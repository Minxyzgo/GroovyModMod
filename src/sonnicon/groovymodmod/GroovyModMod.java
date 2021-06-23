package sonnicon.groovymodmod;

import arc.files.Fi;
import arc.files.ZipFi;
import arc.util.Log;
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

        //Mods.LoadedMod locate = Vars.mods.locateMod("groovymodmod");
        //if(locate == null) throw new IllegalAccessError("Cannot find Groovy Script Loader mod");
        Vars.mods.eachEnabled(mod -> {
            //if(mod.dependencies.contains(locate)) {
                Log.info("[Groovy] Loading mod script from mod @", mod.name);
                try {
                    loadScript(mod.root, mod, gcl);
                } catch (Throwable e) {

                    Log.err("[Groovy] Failed to load mod @. Skipping.", mod.name);
                    Log.err(e);

                }
            //}
        });
    }

    public void loadScript(Fi zip, Mods.LoadedMod mod, GroovyClassLoader gcl) {

        Fi scripts = zip.child("groovy");
        if(scripts.exists() && scripts.isDirectory()) {
            Fi main = zip.child("main.groovy");
            if(main.exists() && !main.isDirectory()) {
                try {
                    GroovyScriptEngine engine = new GroovyScriptEngine(scripts.path(), gcl);
                    engine.loadScriptByName("main.groovy");
                    Log.info("[Groovy] Succeed to load mod: @", mod.name);
                } catch (Exception e) {
                    Log.err("failed to load groovy script. file: " + zip.path());
                    Log.err(e);
                }
            }
        } else {
            Log.warn("Mod: @ didn't have any groovy scripts file", mod.name);
        }
    }

}
