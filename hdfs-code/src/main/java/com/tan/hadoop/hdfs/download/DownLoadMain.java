package com.tan.hadoop.hdfs.download;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.FileOutputStream;

public class DownLoadMain {

    public static void main(String[] args) throws Exception {

        System.setProperty("HADOOP_USER_NAME", "tanbs");

        Configuration conf = new Configuration();

        FileSystem fileSystem = FileSystem.get(conf);

        FSDataInputStream fis = fileSystem.open(new Path("/mkdir/LICENSE.txt"));

        FileOutputStream fos = new FileOutputStream("./LICENSE.txt");
        IOUtils.copyBytes(fis, fos, 1024, true);

        fos.close();
        fis.close();
        fileSystem.close();

    }

}
