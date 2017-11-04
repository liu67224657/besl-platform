package com.enjoyf.platform.service.service.rpc;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RPC {
	
	/**
	 * the n'th parameter to calculate partition (staring from zero). If it's a numeric value, the partition number
	 * is V % N, where N is the total number of nodes which is loaded from env props with the key [<service>.partitions]
	 * For non-primitive arguments, the partition number will be obj.hashcode() % N.
	 * To use ring partition you must first set partition number in XXXServiceSngl. e.g.
	 * <pre>
	 * 	cfg.setConnChooser(new ConnChooserRingPartition(EnvConfig.get().getServicePartitionNum(AccountConstants.SERVICE_SECTION), 3, 10000));
	 * </pre>
	 */
	int partitionHashing() default -1;

	/**
	 * async is not supported yet, Yin said the framework has problems with async
	 * so hold it for now, will try it later 
	 */
	boolean async() default false;
	
}
