package sonnicon.groovymodmod;

import groovy.lang.GroovyClassLoader;
import groovy.util.GroovyScriptEngine;
import mindustry.Vars;
import mindustry.mod.Mod;

import java.io.IOException;
import java.net.MalformedURLException;

public class GroovyModMod extends Mod{
    @Override
    public void init(){
        GroovyClassLoader gcl = new GroovyClassLoader(getClass().getClassLoader());
        Vars.modDirectory.findAll().each(fi -> {
            if(fi.extension().equals("jar") || fi.extension().equals("groovy")){
                try{
                    gcl.addURL(fi.file().toURI().toURL());
                }catch(MalformedURLException e){
                    e.printStackTrace();
                }
            }
        });
        try{
            GroovyScriptEngine gse = new GroovyScriptEngine(Vars.modDirectory.path(), gcl);
            Vars.modDirectory.findAll().each(fi -> {
                if(!fi.extension().equals("groovy")) return;
                try{
                    gse.run(fi.name(), "");
                }catch(Exception e){
                    e.printStackTrace();
                }
            });
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
