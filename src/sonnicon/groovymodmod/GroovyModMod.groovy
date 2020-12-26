package sonnicon.groovymodmod

import mindustry.Vars
import mindustry.mod.Mod

class GroovyModMod extends Mod {
    @Override
    void init() {
        GroovyClassLoader gcl = new GroovyClassLoader(getClass().getClassLoader())
        Vars.modDirectory.findAll().each { if (it.extension() == "jar" || it.extension() == "groovy") gcl.addURL(it.file().toURI().toURL()) }
        GroovyScriptEngine gse = new GroovyScriptEngine(Vars.modDirectory.path(), gcl)
        Vars.modDirectory.findAll().each { if (it.extension() == "groovy") gse.run(it.name(), "") }
    }
}
