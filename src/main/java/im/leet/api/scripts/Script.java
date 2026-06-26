/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.graalvm.polyglot.Context
 *  org.graalvm.polyglot.HostAccess
 */
package im.leet.api.scripts;

import im.leet.Client;
import im.leet.api.scripts.ScriptBindings;
import im.leet.api.scripts.ScriptedModule;
import im.leet.api.scripts.api.bindings.event.IEventProvider;
import im.leet.base.modules.Category;
import im.leet.base.modules.Tag;
import im.leet.utils.LogUtility;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;

public class Script {
    private final String name;
    private final File scriptFile;
    private ScriptedModule scriptedModule;
    private boolean moduleAdded = false;
    private Context scriptctx;
    private IEventProvider eventProvider;

    public Script(String name, File scriptFile) {
        this.name = name;
        this.scriptFile = scriptFile;
    }

    public void init() {
        try {
            this.scriptctx = Context.newBuilder((String[])new String[]{"js"}).allowHostAccess(HostAccess.ALL).allowHostClassLookup(clazz -> clazz.startsWith("im.leet.api.scripts.api.bindings")).allowHostClassLoading(true).allowIO(false).allowNativeAccess(false).option("engine.WarnInterpreterOnly", "false").build();
            ScriptBindings.bind(this.scriptctx, this);
            String source = new String(new FileInputStream(this.scriptFile).readAllBytes());
            this.scriptctx.eval("js", (CharSequence)source);
        }
        catch (Exception e) {
            System.out.println("Error loading script: " + e.getMessage());
        }
    }

    public void register(Map<String, Object> bindings) {
        String name = bindings.get("name").toString();
        String description = bindings.containsKey("description") ? bindings.get("description").toString() : "";
        List authorsList = (List)bindings.get("authors");
        List tags = bindings.containsKey("tags") ? (List)bindings.get("tags") : new ArrayList();
        String[] authors = (String[])authorsList.stream().map(Object::toString).toArray(String[]::new);
        String category = bindings.get("category").toString();
        if (!this.moduleAdded) {
            this.scriptedModule = new ScriptedModule(name, Category.valueOf(category), -1, authors, description);
            LogUtility.debug("size: " + Client.MODULES.getModules().size());
            Client.MODULES.add(this.scriptedModule);
            LogUtility.debug("size: " + Client.MODULES.getModules().size());
            this.moduleAdded = true;
            System.out.printf("Registered script: %s (%s -> %s)%n", this.scriptedModule.getName(), name, category);
        } else {
            this.scriptedModule.setAuthors(authors);
            this.scriptedModule.name = name;
            this.scriptedModule.setDescription(description);
            this.scriptedModule.setCategory(Category.valueOf(category));
            System.out.printf("Reloaded script: %s (%s -> %s)%n", this.scriptedModule.getName(), name, category);
        }
        this.scriptedModule.getTags().clear();
        Iterator iterator = tags.iterator();
        while (iterator.hasNext()) {
            int tag = (Integer)iterator.next();
            this.scriptedModule.addTag(Tag.values()[tag]);
        }
    }

    public String getName() {
        return this.name;
    }

    public File getScriptFile() {
        return this.scriptFile;
    }

    public ScriptedModule getScriptedModule() {
        return this.scriptedModule;
    }

    public boolean isModuleAdded() {
        return this.moduleAdded;
    }

    public Context getScriptctx() {
        return this.scriptctx;
    }

    public IEventProvider getEventProvider() {
        return this.eventProvider;
    }

    public void setEventProvider(IEventProvider eventProvider) {
        this.eventProvider = eventProvider;
    }
}

