package Obe.Util;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
public class LoadBigFile {
	private String text = "";
	private String path = "";
	
    public  String Load() throws Exception {
	   final int BUFFER_SIZE =  0x1200000;// 缓冲区大小为12M
	   
       File f = new File(path);

       /**
        *
        * map(FileChannel.MapMode mode,long position, long size)
        *
        * mode - 根据是按只读、读取/写入或专用（写入时拷贝）来映射文件，分别为 FileChannel.MapMode 类中所定义的
        * READ_ONLY、READ_WRITE 或 PRIVATE 之一
        *
        * position - 文件中的位置，映射区域从此位置开始；必须为非负数
        *
        * size - 要映射的区域大小；必须为非负数且不大于 Integer.MAX_VALUE
        *
        * 所以若想读取文件后半部分内容，如例子所写；若想读取文本后1/8内容，需要这样写map(FileChannel.MapMode.READ_ONLY,
        * f.length()*7/8,f.length()/8)
        *
        * 想读取文件所有内容，需要这样写map(FileChannel.MapMode.READ_ONLY, 0,f.length())
        *
        */

       MappedByteBuffer inputBuffer = new RandomAccessFile(f, "r")
       .getChannel().map(FileChannel.MapMode.READ_ONLY,
                         0, f.length());

       byte[] dst = new byte[BUFFER_SIZE];// 每次读出3M的内容

       long start = System.currentTimeMillis();

       for (int offset = 0; offset < inputBuffer.capacity(); offset += BUFFER_SIZE) {

           if (inputBuffer.capacity() - offset >= BUFFER_SIZE) {

               for (int i = 0; i < BUFFER_SIZE; i++)

                   dst[i] = inputBuffer.get(offset + i);

           } else {

               for (int i = 0; i < inputBuffer.capacity() - offset; i++)

                   dst[i] = inputBuffer.get(offset + i);

           }

           int length = (inputBuffer.capacity() % BUFFER_SIZE == 0) ? BUFFER_SIZE
                        : inputBuffer.capacity() % BUFFER_SIZE;

           text += new String(dst, 0, length);
           //System.out.println(new String(dst, 0, length));// new
           // String(dst,0,length)这样可以取出缓存保存的字符串，可以对其进行操作

       }

       long end = System.currentTimeMillis();

       System.out.println("读取文件文件花费：" + (end - start) + "毫秒");
	return text;
   }

	public void setPath(String path) {
		this.path = path;
	}
}