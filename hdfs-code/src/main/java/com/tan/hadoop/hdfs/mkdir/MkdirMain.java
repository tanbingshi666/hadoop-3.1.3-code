package com.tan.hadoop.hdfs.mkdir;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class MkdirMain {

    public static void main(String[] args) throws Exception {

        System.setProperty("HADOOP_USER_NAME", "tanbs");

        Configuration conf = new Configuration();

        FileSystem fileSystem = FileSystem.get(conf);

        fileSystem.mkdirs(new Path("/mkdir"));

    }

}
