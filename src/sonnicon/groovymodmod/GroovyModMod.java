package sonnicon.groovymodmod;

import arc.files.Fi;
import arc.util.Log;
import arc.util.Time;
import groovy.lang.GroovyClassLoader;
import mindustry.Vars;
import mindustry.mod.Mod;
import mindustry.mod.Mods;

import javax.script.ScriptEngineManager;

public class GroovyModMod extends Mod {
    @Override
    public void loadContent() {
        //GroovyClassLoader gcl = new GroovyClassLoader(getClass().getClassLoader());
        Log.info("Started to load Groovy Script");
        Vars.mods.eachEnabled(mod -> {
            if(mod.meta.dependencies.contains("groovymodmod")) {
                loadScript(mod.root, mod);
            }
        });
    }

    public void loadScript(Fi zip, Mods.LoadedMod mod) {
        Time.mark();
        Fi scripts = zip.child("groovy");
        Log.info("[Groovy] Loading mod script from mod @", mod.name);

        if(scripts.exists() && scripts.isDirectory()) {
            Fi main = scripts.child("main.groovy");
            if(main.exists() && !main.isDirectory()) {
                try {
                    ScriptEngineManager manager = new ScriptEngineManager(getClass().getClassLoader());
                    manager.getEngineByExtension("groovy").eval(main.readString());
                    //gcl.addURL(main.file().toURI().toURL());
                    //GroovyScriptEngine engine = new GroovyScriptEngine(scripts.path(), gcl);
                    //engine.loadScriptByName("/main.groovy");
                    //engine.run(main.name(), "");

//                    new GroovyShell(getClass().getClassLoader()).evaluate(main.readString());
//                    Log.info("[Groovy] Succeed to load mod: @", mod.name);
                } catch (Exception e) {
                    Log.err("failed to load groovy script.");
                    Log.err(e);
                }
            } else {
                Log.info("mod didn't have main groovy script. require: groovy/main.groovy");
            }
        } else {
            Log.info("Mod: @ didn't have any groovy scripts file", mod.name);
        }

        Log.info("[Groovy] load end time: @", Time.elapsed());
    }



}
