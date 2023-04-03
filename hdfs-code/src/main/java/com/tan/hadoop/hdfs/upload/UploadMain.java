package com.tan.hadoop.hdfs.upload;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class UploadMain {

    public static void main(String[] args) throws Exception {

        System.setProperty("HADOOP_USER_NAME", "tanbs");

        Configuration conf = new Configuration();

        FileSystem fileSystem = FileSystem.get(conf);

        fileSystem.copyFromLocalFile(new Path("./LICENSE.txt"), new Path("/mkdir/"));

    }

}
