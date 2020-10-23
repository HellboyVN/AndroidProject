package com.workout.workout.util;

public class FileUtils {
    public static void copyFile(java.io.FileInputStream r7, java.io.FileOutputStream r8) throws java.io.IOException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x001d in list [B:7:0x001a]
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:42)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        /*
        r1 = 0;
        r6 = 0;
        r1 = r7.getChannel();	 Catch:{ all -> 0x0025, all -> 0x0031 }
        r6 = r8.getChannel();	 Catch:{ all -> 0x0025, all -> 0x0031 }
        r2 = 0;	 Catch:{ all -> 0x0025, all -> 0x0031 }
        r4 = r1.size();	 Catch:{ all -> 0x0025, all -> 0x0031 }
        r1.transferTo(r2, r4, r6);	 Catch:{ all -> 0x0025, all -> 0x0031 }
        if (r1 == 0) goto L_0x0018;
    L_0x0015:
        r1.close();	 Catch:{ all -> 0x001e }
    L_0x0018:
        if (r6 == 0) goto L_0x001d;
    L_0x001a:
        r6.close();
    L_0x001d:
        return;
    L_0x001e:
        r0 = move-exception;
        if (r6 == 0) goto L_0x0024;
    L_0x0021:
        r6.close();
    L_0x0024:
        throw r0;
    L_0x0025:
        r0 = move-exception;
        if (r1 == 0) goto L_0x002b;
    L_0x0028:
        r1.close();	 Catch:{ all -> 0x0025, all -> 0x0031 }
    L_0x002b:
        if (r6 == 0) goto L_0x0030;
    L_0x002d:
        r6.close();
    L_0x0030:
        throw r0;
    L_0x0031:
        r0 = move-exception;
        if (r6 == 0) goto L_0x0037;
    L_0x0034:
        r6.close();
    L_0x0037:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.workout.workout.util.FileUtils.copyFile(java.io.FileInputStream, java.io.FileOutputStream):void");
    }
}
