package com.zhenhui.demo.uac.service.manager;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Component
public class CaptchaManager implements InitializingBean {

    @Autowired
    private CacheManager cacheManager;

    private Cache cache;

    private final static RandomStringGenerator generator = new RandomStringGenerator.Builder()
            .withinRange('0', '9').build();

    @Override
    public void afterPropertiesSet() throws Exception {
        cache = cacheManager.getCache("registry_captchas");
    }

    public String createRegistryCaptcha(String phone, boolean create) {

        String captcha = null;

        if (create) {
            captcha = generator.generate(4);
            Element element = new Element(phone, captcha, true, 300, 300);
            cache.put(element);
        } else {
            lookupRegistryCaptcha(phone);
        }

        return captcha;
    }

    public String lookupRegistryCaptcha(String phone) {
        Element element = cache.get(phone);
        if (element != null && !element.isExpired()) {
            return (String) element.getObjectValue();
        }

        return null;
    }

    public void invalidRegistryCaptcha(String phone) {
        cache.remove(phone);
    }

}
