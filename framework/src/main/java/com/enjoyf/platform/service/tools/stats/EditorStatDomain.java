/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.tools.stats;

import com.enjoyf.platform.service.stats.AbstractStatDomain;
import com.enjoyf.platform.service.stats.StatDomainPrefix;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-9-17 下午8:17
 * Description:
 */
public class EditorStatDomain extends AbstractStatDomain {
    //section's format is <admin uno>/<content id|sub type|all>
    public static EditorStatDomain EDITOR_ARTICLE_PV = new EditorStatDomain("editor.art.pv", true);

    //section's format is <admin uno>/<content id|sub type|all>
    public static EditorStatDomain EDITOR_ARTICLE_UV = new EditorStatDomain("editor.art.uv", true);

    //section's format is <admin uno>/<content id|sub type|all>
    public static EditorStatDomain EDITOR_ARTICLE_CPV = new EditorStatDomain("editor.art.cpv", true);

    //section's format is <admin uno>/<content id|sub type|all>
    public static EditorStatDomain EDITOR_ARTICLE_CMT = new EditorStatDomain("editor.art.cmt", true);

    //section's format is <admin uno>/<sub type|all>
    public static EditorStatDomain EDITOR_ARTICLE_POST = new EditorStatDomain("editor.art.post", true);

    //section's format is <admin uno>/<game id|all>
    public static EditorStatDomain EDITOR_GAME_PV = new EditorStatDomain("editor.game.pv", true);

    //section's format is <admin uno>/<game id|all>
    public static EditorStatDomain EDITOR_GAME_UV = new EditorStatDomain("editor.game.uv", true);

    //section's format is <admin uno>/<all>
    public static EditorStatDomain EDITOR_GAME_CREATE = new EditorStatDomain("editor.game.create", true);

    protected EditorStatDomain(String c, boolean multi) {
        super(StatDomainPrefix.DOMAIN_PREFIX_USER_EVENT.getCode() + StatDomainPrefix.KEY_DOMAIN_SEPARATOR + c, multi);
    }
}
