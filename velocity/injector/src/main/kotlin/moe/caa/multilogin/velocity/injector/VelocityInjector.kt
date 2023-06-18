package moe.caa.multilogin.velocity.injector

import com.velocitypowered.proxy.protocol.MinecraftPacket
import com.velocitypowered.proxy.protocol.StateRegistry
import com.velocitypowered.proxy.protocol.StateRegistry.PacketRegistry
import moe.caa.multilogin.core.openAccess
import moe.caa.multilogin.loader.core.IMultiCore
import moe.caa.multilogin.loader.injector.IInjector
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.util.function.Supplier


object VelocityInjector : IInjector {
    private val mapPut: Lazy<Method> = lazy {
        MutableMap::class.java
            .getMethod("put", Any::class.java, Any::class.java)
    }


    override fun inject(api: IMultiCore) {
        // auth
        val stateRegistry = StateRegistry.LOGIN
        val serverboundField: Field = StateRegistry::class.java.getDeclaredField("serverbound").openAccess()
        val serverbound = serverboundField[stateRegistry] as PacketRegistry


//        redirectInput(serverbound, EncryptionResponse::class.java) { error("todo") }
//        redirectInput(serverbound, ServerLogin::class.java) { error("todo") }
    }

    /**
     * 重定向数据包
     *
     * @param bound            数据包方向
     * @param originalClass    原始数据包类对象
     * @param supplierRedirect 重定向后的 Supplier
     */
    private fun <T> redirectInput(
        bound: PacketRegistry,
        originalClass: Class<T>,
        supplierRedirect: Supplier<out T>
    ) {
        val packetIdToSupplierField: Field = PacketRegistry.ProtocolRegistry::class.java
            .getDeclaredField("packetIdToSupplier").openAccess()

        getProtocolRegistries(bound).forEach { protocolRegistry ->
            val packetIdToSupplier =
                packetIdToSupplierField.get(protocolRegistry) as Map<*, *> // IntObjectMap<Supplier<? extends MinecraftPacket>>
            packetIdToSupplier.forEach {
                val minecraftPacketObject: MinecraftPacket = (it.value as Supplier<*>).get() as MinecraftPacket
                // 类匹配则进行替换
                if (minecraftPacketObject::class.java == originalClass) {
                    (it as MutableMap.MutableEntry).setValue(supplierRedirect)
                }
            }
        }
    }

    /**
     * 追加注册出口包
     *
     * @param bound         数据包方向
     * @param originalClass 原始数据包类对象
     * @param appendClass   追加的数据包类对象
     */
    private fun <T> redirectOutput(
        bound: PacketRegistry,
        originalClass: Class<T>,
        appendClass: Class<out T>
    ) {
        val packetClassToIdField: Field = PacketRegistry.ProtocolRegistry::class.java
            .getDeclaredField("packetClassToId").openAccess()

        getProtocolRegistries(bound).forEach {
            val packetClassToId =
                packetClassToIdField.get(it) as MutableMap<*, *> // Object2IntMap<Class<? extends MinecraftPacket>> -> Map<Class<? extends MinecraftPacket>, Integer>
            if (packetClassToId.containsKey(originalClass)) mapPut.value.invoke(
                packetClassToId,
                appendClass,
                packetClassToId[originalClass]
            )
        }
    }

    private fun getProtocolRegistries(bound: PacketRegistry) = (PacketRegistry::class.java
        //Map<ProtocolVersion, ProtocolRegistry> versions
        .getDeclaredField("versions").openAccess().get(bound) as Map<*, *>).values
}