package com.gracerun.security.authentication.core.json;//package com.gracerun.auth.core.json;
//
//import com.gracerun.auth.bean.UserContext;
//import com.fasterxml.jackson.core.Version;
//import com.fasterxml.jackson.databind.module.SimpleModule;
//import org.springframework.security.jackson2.SecurityJackson2Modules;
//
//public class UsernameJackson2Module extends SimpleModule {
//
//    public UsernameJackson2Module() {
//        super(UsernameJackson2Module.class.getName(), new Version(1, 0, 0, null, null, null));
//    }
//
//    @Override
//    public void setupModule(SetupContext context) {
//        SecurityJackson2Modules.enableDefaultTyping(context.getOwner());
//        context.setMixInAnnotations(UserContext.class, UsernameMixin.class);
//    }
//}
