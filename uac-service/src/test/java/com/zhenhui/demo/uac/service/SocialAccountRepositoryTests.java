package com.zhenhui.demo.uac.service;

import com.zhenhui.demo.uac.core.dataobject.SocialAccount;
import com.zhenhui.demo.uac.common.SocialType;
import com.zhenhui.demo.uac.core.repository.SocialAccountRepository;
import com.zhenhui.demo.uac.core.repository.exception.UserAlreadyExistsException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class SocialAccountRepositoryTests {

    @Autowired
    private SocialAccountRepository socialAccountRepository;

    @Test
    public void testSocialAccountQuery() {

        SocialAccount account = socialAccountRepository.getSocialAccount(SocialType.valueOf(1), 10001L);
        assertNotNull(account);

        List<SocialAccount> accounts = socialAccountRepository.getSocialAccounts(1L);
        assertTrue(accounts.size() > 0);

    }

    @Test
    public void testSocialAccountUpdate() {

        SocialAccount account = socialAccountRepository.getSocialAccount(SocialType.valueOf(1), 10001L);
        assertNotNull(account);

        List<SocialAccount> accounts = socialAccountRepository.getSocialAccounts(1L);
        assertTrue(accounts.size() > 0);

        assertEquals("jerry", account.getNickname());
        assertEquals("", account.getAvatar());

        assertTrue(accounts.stream().anyMatch(e -> "jerry".equals(e.getNickname())));
        assertTrue(accounts.stream().anyMatch(e -> "".equals(e.getAvatar())));

        socialAccountRepository.updateNicknameAvatar(SocialType.valueOf(1), 10001L, "zhenhui", "http://www.qqxoo.com/uploads/allimg/180210/1013332O5-9.jpg");

        account = socialAccountRepository.getSocialAccount(SocialType.valueOf(1), 10001L);
        assertEquals("zhenhui", account.getNickname());
        assertEquals("http://www.qqxoo.com/uploads/allimg/180210/1013332O5-9.jpg", account.getAvatar());

        accounts = socialAccountRepository.getSocialAccounts(1L);
        assertTrue(accounts.size() > 0);

        assertTrue(accounts.stream().anyMatch(e -> "zhenhui".equals(e.getNickname())));
        assertTrue(accounts.stream().anyMatch(e -> "http://www.qqxoo.com/uploads/allimg/180210/1013332O5-9.jpg".equals(e.getAvatar())));

        long originUserId = account.getUserId();

        socialAccountRepository.updateUserId(SocialType.valueOf(1), 10001L, originUserId, 2L);

        account = socialAccountRepository.getSocialAccount(SocialType.valueOf(1), 10001L);
        assertEquals(2L, account.getUserId().longValue());

        accounts = socialAccountRepository.getSocialAccounts(1L);
        assertTrue(accounts.size() == 0);

        accounts = socialAccountRepository.getSocialAccounts(2L);
        assertTrue(accounts.size() > 0);
        assertTrue(accounts.stream().anyMatch(e -> 2L == e.getUserId()));

        socialAccountRepository.updateUserId(SocialType.valueOf(1), 10001L, 2L, 1L);

    }

    @Test
    public void testSocialAccountCreate() throws Exception {

        SocialAccount account = new SocialAccount();
        account.setType(SocialType.QQ);
        account.setOpenId(274425775L);
        account.setToken("token-token-token");
        account.setNickname("NULL");
        account.setAvatar("");
        account.setActivated(true);
        account.setGmtCreate(new Date());
        account.setGmtModified(new Date());

        socialAccountRepository.createSocialAccount(account);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void testSocialAccountCreateDuplicate() throws Exception {

        SocialAccount account = new SocialAccount();
        account.setType(SocialType.QQ);
        account.setOpenId(20001L);
        account.setToken("token-token-token");
        account.setNickname("NULL");
        account.setAvatar("");
        account.setActivated(true);
        account.setGmtCreate(new Date());
        account.setGmtModified(new Date());

        for (int i = 0; i < 2; ++i){
            socialAccountRepository.createSocialAccount(account);
        }
    }


}


