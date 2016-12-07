package org.apache.storm.storm_core;


import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import org.apache.storm.utils.Utils;

public class Top3App 
{
    public static final String LANG ="langs";

	
    public static void main( String[] args )
    {
    	
    	String[] langs = new String[]{"es","en"}; //take from input.
    	
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("currencySpout", new HashtagSpout());
        builder.setBolt("converterBolt", new ConditionalWindowBolt(),langs.length)
        .fieldsGrouping("currencySpout", HashtagSpout.NORMALSTREAM, new Fields(LANG));
        
        
        //Possible further parallelization: Send from first language bolt towards a bolt that counts
        //the words, switch from bolt to bolt for each tweet (modulus bolt) and starts counting as soon as possible
        //so the counting is distributed. Then, unite those bolts in another final bolt per language 
        
        Config conf = new Config();
        //conf.setNumWorkers(langs.length); //Number of working nodes
        //conf.setDebug(true); //To debug. Remove when deployment
        
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("converterTopology",conf , builder.createTopology());

        Utils.sleep(10000);
              		       

        cluster.killTopology("converterTopology");

        cluster.shutdown();
    }
}