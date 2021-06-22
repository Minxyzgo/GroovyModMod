package sonnicon.groovymodmod;

import arc.files.Fi;
import arc.files.ZipFi;
import arc.util.Log;
import groovy.lang.GroovyClassLoader;
import groovy.util.GroovyScriptEngine;
import mindustry.Vars;
import mindustry.mod.Mod;
public class GroovyModMod extends Mod {
    @Override
    public void loadContent() {
        GroovyClassLoader gcl = new GroovyClassLoader(getClass().getClassLoader());
        for(Fi file : Vars.modDirectory.list()){
            if(!file.extension().equals("jar") && !file.extension().equals("zip") && !(file.isDirectory() && (file.child("mod.json").exists() || file.child("mod.hjson").exists()))) continue;

            Log.debug("[Groovy] Loading mod script @", file);
            try{
                loadScript(file, gcl);
            }catch(Throwable e) {

                Log.err("Failed to load groovy Script @. Skipping.", file);
                Log.err(e);

            }
        }
    }

    public void loadScript(Fi source, GroovyClassLoader gcl) {
        Fi zip = source.isDirectory() ? source : new ZipFi(source);
        if(zip.list().length == 1 && zip.list()[0].isDirectory()){
            zip = zip.list()[0];
        }

        Fi scripts = zip.child("groovy");
        if(scripts.exists() && scripts.isDirectory()) {
            Fi main = zip.child("main.groovy");
            if(main.exists() && !main.isDirectory()) {
                try {
                    GroovyScriptEngine engine = new GroovyScriptEngine(scripts.path(), gcl);
                    engine.loadScriptByName("main.groovy");
                } catch (Exception e) {
                    Log.err("failed to load groovy script. file: " + zip.path());
                    Log.err(e);
                }
            }
        }
    }

}
