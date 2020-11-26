package com.tingyu.venus.utils.threadpool;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 核心业务处理线程池，单例模式
 */
public class SingleThreadPoolExecutor extends ThreadPoolExecutor {
    //1.构造方法私有化

    private static volatile SingleThreadPoolExecutor instance;

    //线程池的七大参数

    /**
     * int corePoolSize,
     * int maximumPoolSize,
     * long keepAliveTime,
     * TimeUnit unit,
     * BlockingQueue<Runnable> workQueue,
     * ThreadFactory threadFactory,
     * RejectedExecutionHandler handler
     W/om.tingyu.venu: Accessing hidden method Landroid/view/View;->computeFitSystemWindows(Landroid/graphics/Rect;Landroid/graphics/Rect;)Z (greylist, reflection, allowed)
     W/om.tingyu.venu: Accessing hidden method Landroid/view/ViewGroup;->makeOptionalFitsSystemWindows()V (greylist, reflection, allowed)
     I/MessageService: StartCommand MessageService...
     D/MessageService: SingleThreadPool-thread-1与服务器建立 websocket 连接
     D/HostConnection: HostConnection::get() New Host Connection established 0xf7270440, tid 19114
     D/HostConnection: HostComposition ext ANDROID_EMU_CHECKSUM_HELPER_v1 ANDROID_EMU_native_sync_v2 ANDROID_EMU_native_sync_v3 ANDROID_EMU_native_sync_v4 ANDROID_EMU_dma_v1 ANDROID_EMU_direct_mem ANDROID_EMU_host_composition_v1 ANDROID_EMU_host_composition_v2 ANDROID_EMU_vulkan ANDROID_EMU_deferred_vulkan_commands ANDROID_EMU_vulkan_null_optional_strings ANDROID_EMU_vulkan_create_resources_with_requirements ANDROID_EMU_YUV_Cache ANDROID_EMU_async_unmap_buffer ANDROID_EMU_vulkan_ignored_handles ANDROID_EMU_vulkan_free_memory_sync ANDROID_EMU_vulkan_shader_float16_int8 ANDROID_EMU_vulkan_async_queue_submit GL_OES_vertex_array_object GL_KHR_texture_compression_astc_ldr ANDROID_EMU_gles_max_version_2
     W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
     W/om.tingyu.venu: Accessing hidden method Ldalvik/system/CloseGuard;->get()Ldalvik/system/CloseGuard; (greylist,core-platform-api, reflection, allowed)
     Accessing hidden method Ldalvik/system/CloseGuard;->open(Ljava/lang/String;)V (greylist,core-platfor
     */
    private static final int corePoolSize = Runtime.getRuntime().availableProcessors();
    private static final int maximumPoolSize = corePoolSize << 2;

    private static final long keepAliveTime = 60L;
    private static final int workQueueSize = 100;


    private SingleThreadPoolExecutor() {
        super(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, new LinkedBlockingDeque<>(workQueueSize),new CustomThreadFactory());
    }

    //2.对外提供实例接口

    public static SingleThreadPoolExecutor newInstance() {


        if (instance == null) {

            synchronized (SingleThreadPoolExecutor.class) {
                if (instance == null) {
                    instance = new SingleThreadPoolExecutor();
                }
            }

        }
        return instance;

    }


    /**
     * 自定义线程工厂
     */
    static class CustomThreadFactory implements ThreadFactory{

        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        public CustomThreadFactory(){
            SecurityManager var1 = System.getSecurityManager();
            this.group = var1 != null ? var1.getThreadGroup() : Thread.currentThread().getThreadGroup();
            this.namePrefix = "SingleThreadPool-thread-";
        }

        @Override
        public Thread newThread(Runnable var1) {
            Thread var2 = new Thread(this.group, var1, this.namePrefix + this.threadNumber.getAndIncrement(), 0L);

            if (var2.isDaemon()) {
                var2.setDaemon(false);
            }

            if (var2.getPriority() != 5) {
                var2.setPriority(5);
            }

            return var2;
        }
    }


}
