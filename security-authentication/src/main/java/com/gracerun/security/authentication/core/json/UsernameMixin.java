package com.gracerun.security.authentication.core.json;//package com.gracerun.auth.core.json;
//
//import com.fasterxml.jackson.annotation.*;
//import org.springframework.security.core.GrantedAuthority;
//
//import java.util.Collection;
//
//@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
//@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE,
//        isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.ANY)
//@JsonIgnoreProperties(ignoreUnknown = true)
//abstract class UsernameMixin {
//
//    @JsonCreator
//    public UsernameMixin(@JsonProperty("authorities") Collection<? extends GrantedAuthority> authorities) {
//    }
//}
