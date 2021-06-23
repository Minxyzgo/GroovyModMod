package sonnicon.groovymodmod;

import arc.files.Fi;
import arc.files.ZipFi;
import arc.util.Log;
import arc.util.Time;
import groovy.lang.GroovyClassLoader;
import groovy.util.GroovyScriptEngine;
import mindustry.Vars;
import mindustry.mod.Mod;
import mindustry.mod.Mods;

public class GroovyModMod extends Mod {
    @Override
    public void loadContent() {
        GroovyClassLoader gcl = new GroovyClassLoader(getClass().getClassLoader());
        Log.info("Started to load Groovy Script");
        Vars.mods.eachEnabled(mod -> {
            if(mod.meta.dependencies.contains("groovymodmod")) {
                loadScript(mod.root, mod, gcl);
            }
        });
    }

    public void loadScript(Fi zip, Mods.LoadedMod mod, GroovyClassLoader gcl) {
        Time.mark();
        Fi scripts = zip.child("groovy");
        Log.info("[Groovy] Loading mod script from mod @", mod.name);

        if(scripts.exists() && scripts.isDirectory()) {
            Fi main = scripts.child("main.groovy");
            if(main.exists() && !main.isDirectory()) {
                try {
                    GroovyScriptEngine engine = new GroovyScriptEngine(scripts.path(), gcl);
                    engine.loadScriptByName("main.groovy");
                    Log.info("[Groovy] Succeed to load mod: @", mod.name);
                } catch (Exception e) {
                    Log.err("failed to load groovy script. file: " + zip.path());
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
