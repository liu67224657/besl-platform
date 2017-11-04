package com.enjoyf.platform.service.joymeapp;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-12-11 上午10:01
 * Description:
 */
public class AccountVirtualType implements Serializable {

    private static Map<Integer, AccountVirtualType> map = new HashMap<Integer, AccountVirtualType>();

    //水军
    public static final AccountVirtualType DEFAULT = new AccountVirtualType(0);

    //运营
    public static final AccountVirtualType FORMAL  = new AccountVirtualType(1);

	//手游排行榜
	public static final AccountVirtualType MOBILE_GAME_RANKING  = new AccountVirtualType(2);

    //玩霸问题-虚拟用户
    public static final AccountVirtualType WANBA_ASK  = new AccountVirtualType(3);

    private int code;

    private AccountVirtualType(int c) {
        this.code = c;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "AppDisplayType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((AccountVirtualType) o).code) return false;

        return true;
    }

    public static AccountVirtualType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<AccountVirtualType> getAll() {
        return map.values();
    }
}
