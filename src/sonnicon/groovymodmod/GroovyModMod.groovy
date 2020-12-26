package sonnicon.groovymodmod

import mindustry.Vars
import mindustry.mod.Mod

class GroovyModMod extends Mod{
    @Override
    void init(){
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
