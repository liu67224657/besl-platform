package com.enjoyf.platform.serv.content.processor;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.WallHotdeployConfig;
import com.enjoyf.platform.service.content.AppsContent;
import com.enjoyf.platform.service.content.AudioContent;
import com.enjoyf.platform.service.content.Content;
import com.enjoyf.platform.service.content.DiscoveryWallContent;
import com.enjoyf.platform.service.content.ImageContent;
import com.enjoyf.platform.service.content.VideoContent;
import com.enjoyf.platform.service.content.wall.WallBlock;
import com.enjoyf.platform.service.content.wall.WallBlockStyleConfig;
import com.enjoyf.platform.service.content.wall.WallBlockType;
import com.enjoyf.platform.service.content.wall.WallContentRule;
import com.enjoyf.platform.service.content.wall.WallContentType;
import com.enjoyf.platform.service.content.wall.WallLayout;
import com.enjoyf.platform.service.content.wall.WallLayoutConfig;
import com.enjoyf.platform.service.profile.Profile;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.text.ImageResolveUtil;
import com.enjoyf.platform.text.ResolveContent;
import com.enjoyf.platform.text.TextProcessorFatctory;
import com.enjoyf.platform.text.WordProcessorKey;
import com.enjoyf.platform.text.processor.SubStringTextProcessor;
import com.enjoyf.platform.text.processor.TextProcessor;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-3-19
 * Time: 下午3:39
 * To change this template use File | Settings | File Templates.
 */
public class WallContentProcessor {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(WallHotdeployConfig.class);

    //filter content
    public Map<Integer, DiscoveryWallContent> filterContents(List<Content> contents, Map<Integer, DiscoveryWallContent> wallContentMap) {
        WallHotdeployConfig wallConfig = HotdeployConfigFactory.get().getConfig(WallHotdeployConfig.class);

        if (wallContentMap.size() > 0) {
            //重新初始Map
            Map<Integer, DiscoveryWallContent> tmpWallContentMap = new HashMap<Integer, DiscoveryWallContent>();
            for (DiscoveryWallContent wallContent : wallContentMap.values()) {
                tmpWallContentMap.put(tmpWallContentMap.size() + 1, wallContent);
            }
            wallContentMap = tmpWallContentMap;
        }

        // blacklist
        Map<String, String> blacklistMap = wallConfig.getWallBlackListMap();
        Set<String> unoSet = new HashSet<String>();
        List<DiscoveryWallContent> wallContentList = new ArrayList<DiscoveryWallContent>();
        int proportion = HotdeployConfigFactory.get().getConfig(WallHotdeployConfig.class).getWallWallBlockImageProportion();
        for (Content content : contents) {
            //系统黒名单
            if (blacklistMap.get(content.getUno()) != null) {
                continue;
            }

            if (!content.getAuditStatus().isTextPass()) {
                continue;
            }

            DiscoveryWallContent wallContent = new DiscoveryWallContent();
            wallContent.setContentId(content.getContentId());
            wallContent.setContentUno(content.getUno());
            wallContent.setWallSubject(content.getSubject());
            wallContent.setWallContent(content.getContent());
            wallContent.setWallTag(content.getContentTag());
            wallContent.setPublishDate(content.getPublishDate());
            wallContent.setContentType(content.getContentType());

            WallContentType wallContentType = null;
            boolean hasMulti = false;

            if (content.getContentType().hasApp()) {
                hasMulti = true;
                Iterator<AppsContent> it = content.getApps().getApps().iterator();
                if (it.hasNext()) {
                    wallContent.setThumbImgLink(it.next().getAppSrc());
                    wallContentType = WallContentType.APP;
                }
            }

            if (wallContentType == null && content.getContentType().hasImage()) {
                hasMulti = true;
                for (ImageContent imageContent : content.getImages().getImages()) {
                    if (imageContent.getValidStatus()) {
                        if (imageContent.getW() != 0 && imageContent.getH() != 0) {
                            //new image
                            if (!(imageContent.getW() <= 100 && imageContent.getH() <= 100)) {

                                if (imageContent.getW() >= imageContent.getH()) {
                                    wallContentType = WallContentType.IMAGE_H;
                                } else if (imageContent.getH() > imageContent.getW()) {
                                    wallContentType = WallContentType.IMAGE_V;
                                }

                                if (wallContentType != null) {
                                    wallContent.setThumbImgLink(imageContent.getM());
                                    wallContent.setWidth(imageContent.getW());
                                    wallContent.setHeight(imageContent.getH());
                                    break;
                                }
                            }

                        } else {
                            //old image
                            wallContent.setThumbImgLink(imageContent.getM());
                            wallContent.setWidth(imageContent.getW());
                            wallContent.setHeight(imageContent.getH());
                            wallContentType = WallContentType.IMAGE_H;
                            break;
                        }
                    }
                }
            }

            if (wallContentType == null && content.getContentType().hasVideo()) {
                hasMulti = true;
                Iterator<VideoContent> it = content.getVideos().getVideos().iterator();
                if (it.hasNext()) {
                    wallContent.setThumbImgLink(it.next().getUrl());
                    wallContentType = WallContentType.VIDEO;
                }
            }

            if (wallContentType == null && content.getContentType().hasAudio()) {
                hasMulti = true;
                Iterator<AudioContent> it = content.getAudios().getAudios().iterator();
                if (it.hasNext()) {
                    wallContent.setThumbImgLink(ImageResolveUtil.parseAudioM(it.next().getUrl()));
                    wallContentType = WallContentType.AUDIO;
                }
            }

            if (wallContentType == null && content.getContentType().hasText() && content.getAuditStatus().isTextPass() && !hasMulti) {
                if (!StringUtil.isEmpty(content.getSubject()) || !StringUtil.isEmpty(content.getContent())) {
                    wallContentType = WallContentType.TEXT;
                }
            }

            if (wallContentType != null) {
                wallContent.setWallContentType(wallContentType);

                unoSet.add(content.getUno());
                wallContentList.add(wallContent);
            }


        }

        try {
            Map<String, Profile> profileMap = ProfileServiceSngl.get().queryProfilesByUnosMap(unoSet);

            for (DiscoveryWallContent wallContent : wallContentList) {
                if (profileMap.get(wallContent.getContentUno()) != null) {
                    wallContent.setDomainName(profileMap.get(wallContent.getContentUno()).getBlog().getDomain());
                    wallContent.setScreenName(profileMap.get(wallContent.getContentUno()).getBlog().getScreenName());
                    if (profileMap.get(wallContent.getContentUno()).getDetail().getVerifyType() != null) {
                        wallContent.setVerifyType(profileMap.get(wallContent.getContentUno()).getDetail().getVerifyType());
                    }

                    wallContentMap.put(wallContentMap.size() + 1, wallContent);
                }

            }

        } catch (Exception e) {
            //
            GAlerter.lab("ContentLogic initWall call ProfileServiceSngl getProfileBlogByUno error.", e);
        }

        return wallContentMap;
    }

    // first init
    public Map<Integer, WallLayout> initWallLayout(Map<Integer, DiscoveryWallContent> wallContentMap, Map<Integer, WallLayout> initLayoutMap, int currentMapSize, int totalNum) {
        // wall config
        WallHotdeployConfig wallConfig = HotdeployConfigFactory.get().getConfig(WallHotdeployConfig.class);
        // layout total display size
        int totalDisplaySize = totalNum - initLayoutMap.size();
        // layout display rule
        int layoutDisplayRule = wallConfig.getWallLayoutDisplayRule();
        // layout type
        Map<Integer, WallLayoutConfig> wallLayoutConfigMap = wallConfig.getWallLayoutConfigMap();

        int wallContentIndex = 1; //记录wallContentMap 的 keyIndex
        boolean hasWallContent = true;
        while (totalDisplaySize > 0 && hasWallContent) {
            WallLayoutConfig wallLayoutConfig = new WallLayoutConfig();
            WallLayout wallLayout = new WallLayout();
            // 加载版面
            if (layoutDisplayRule == 0) {
                //顺序
                int layoutTypeIdx = currentMapSize % wallLayoutConfigMap.size();
                wallLayoutConfig = wallLayoutConfigMap.get(layoutTypeIdx);
                currentMapSize++;
            } else if (layoutDisplayRule == 1) {
                //随机
                Random random = new Random();
                int layoutTypeIdx = random.nextInt(wallLayoutConfigMap.size());
                wallLayoutConfig = wallLayoutConfigMap.get(layoutTypeIdx);
            }

            Map<Integer, WallBlock> map = new LinkedHashMap<Integer, WallBlock>();

            wallLayout.setLayoutType(wallLayoutConfig.getLayoutType());
            for (WallBlockType wallBlockType : wallLayoutConfig.getWallBlockTypeList()) {
                WallBlock block = null;
                do {
                    if (wallContentMap.containsKey(wallContentIndex)) {
                        block = new WallBlock();
                        block.setWallBlockType(wallBlockType);
                        block = processWallBlock(wallContentMap.get(wallContentIndex), block);
                        if (block != null) {
                            map.put(wallContentIndex, block);
                            wallLayout.getWallBlockList().add(block);
                        }
                        wallContentIndex++;
                    } else {
                        hasWallContent = false;
                        break;
                    }
                } while (block == null);
            }

            // one layout init over
            if (map.size() == wallLayoutConfig.getWallBlockTypeList().size()) {
                //清除wallContent
                for (int idx : map.keySet()) {
                    wallContentMap.remove(idx);
                }
                initLayoutMap.put(totalDisplaySize, wallLayout);
                totalDisplaySize--;
            }
        }

        return initLayoutMap;
    }

    // reload
    public Map<Integer, WallLayout> reloadWallLayout(Map<Integer, DiscoveryWallContent> wallContentMap, Map<Integer, WallLayout> layoutMap, int currentMapSize) {
        // wall config
        WallHotdeployConfig wallConfig = HotdeployConfigFactory.get().getConfig(WallHotdeployConfig.class);
        // layout total display size
        int totalDisplaySize = wallConfig.getWallLayoutTotalDisplaySize();
        // layout display rule
        int layoutDisplayRule = wallConfig.getWallLayoutDisplayRule();
        // layout type
        Map<Integer, WallLayoutConfig> wallLayoutConfigMap = wallConfig.getWallLayoutConfigMap();

        int wallContentIndex = 1; //记录wallContentMap 的 index
        boolean hasWallContent = true;
        while (hasWallContent) {
            WallLayoutConfig wallLayoutConfig = new WallLayoutConfig();
            WallLayout wallLayout = new WallLayout();

            if (layoutDisplayRule == 0) {
                //顺序
                int layoutTypeIdx = currentMapSize % wallLayoutConfigMap.size();
                wallLayoutConfig = wallLayoutConfigMap.get(layoutTypeIdx);
                wallLayout.setLayoutType(wallLayoutConfig.getLayoutType());
                currentMapSize++;

            } else if (layoutDisplayRule == 1) {
                //随机
                Random random = new Random();
                int layoutTypeIdx = random.nextInt(wallLayoutConfigMap.size());
                wallLayoutConfig = wallLayoutConfigMap.get(layoutTypeIdx);
                wallLayout.setLayoutType(wallLayoutConfig.getLayoutType());
            }

            Map<Integer, WallBlock> map = new LinkedHashMap<Integer, WallBlock>();
            for (WallBlockType wallBlockType : wallLayoutConfig.getWallBlockTypeList()) {
                WallBlock block = null;
                do {
                    if (wallContentMap.containsKey(wallContentIndex)) {
                        block = new WallBlock();
                        block.setWallBlockType(wallBlockType);
                        block = processWallBlock(wallContentMap.get(wallContentIndex), block);
                        if (block != null) {
                            map.put(wallContentIndex, block);
                            wallLayout.getWallBlockList().add(block);
                        } else {
                            logger.debug("+++++++++++null+++++++++++++++" + wallContentIndex);
                        }
                        wallContentIndex++;
                    } else {
                        hasWallContent = false;
                        break;
                    }
                } while (block == null);
            }

            // one layout init over
            if (map.size() == wallLayoutConfig.getWallBlockTypeList().size()) {
                //清除wallContent
                for (int idx : map.keySet()) {
                    wallContentMap.remove(idx);
                }

                layoutMap.put(layoutMap.size() + 1, wallLayout);
                if (layoutMap.size() > totalDisplaySize) {
                    layoutMap.remove(layoutMap.size() - totalDisplaySize);
                }
            }
        }

        return layoutMap;
    }

    private WallBlock processWallBlock(DiscoveryWallContent wallContent, WallBlock wallBlock) {
        WallHotdeployConfig wallConfig = HotdeployConfigFactory.get().getConfig(WallHotdeployConfig.class);

        wallContent = processWallContentRule(wallContent);

        Map<Integer, String> blockStyleMap = wallConfig.getWallBlockStyleMap(wallContent.getWallContentType(), wallBlock.getWallBlockType(), wallContent.getWallContentRule());

        if (blockStyleMap != null && blockStyleMap.size() > 0) {
            Random random = new Random();
            String style = blockStyleMap.get(random.nextInt(blockStyleMap.size()));
            wallBlock.setStyle(style);

            WallBlockStyleConfig styleConfig = wallConfig.getWallBlockStyleConfig(wallContent.getWallContentType(), wallBlock.getWallBlockType(), wallContent.getWallContentRule(), style);

            if (styleConfig != null) {
                wallContent = processDiscoveryWallContent(wallContent, styleConfig);
                wallBlock.setDiscoveryWallContent(wallContent);
                wallBlock.setWallContentType(wallContent.getWallContentType());
                wallBlock.setStyleConfig(styleConfig);
            } else {
                if (logger.isDebugEnabled()) {
                    String key = wallContent.getWallContentType().getCode() + "." + wallBlock.getWallBlockType().getCode() + "." + wallContent.getWallContentRule().getCode() + "." + style;
                    logger.debug("wallConfig.getWallBlockStyleConfig error:" + key);
                }
                GAlerter.lab("WallContentProcessor call processWallBlock  wallConfig.getWallBlockStyleConfig error.");
            }
        } else {
            if (logger.isDebugEnabled()) {
                String key = wallContent.getWallContentType().getCode() + "." + wallBlock.getWallBlockType().getCode() + "." + wallContent.getWallContentRule().getCode();
                logger.debug("wallConfig.getWallBlockStyleConfig error:" + key);
            }
            GAlerter.lab("WallContentProcessor call processWallBlock wallConfig.getWallBlockStyleMap error.");
            return null;
        }

        return wallBlock;
    }

    private DiscoveryWallContent processWallContentRule(DiscoveryWallContent wallContent) {
        wallContent.setWallContentRule(WallContentRule.NONE);

        ResolveContent resolveContent = new ResolveContent();
        resolveContent.setContent(wallContent.getWallContent());

        TextProcessor textProcessor = TextProcessorFatctory.get().getProcessorByKey(WordProcessorKey.KEY_DISCOVERY_SUBJECT);
        if (textProcessor != null) {
            resolveContent = textProcessor.process(resolveContent);
        }

        if (!StringUtil.isEmpty(wallContent.getWallSubject()) && !StringUtil.isEmpty(resolveContent.getContent())) {
            wallContent.setWallContentRule(WallContentRule.SC);
        } else if (!StringUtil.isEmpty(wallContent.getWallSubject()) || !StringUtil.isEmpty(resolveContent.getContent())) {
            wallContent.setWallContentRule(WallContentRule.SORC);

            if (StringUtil.isEmpty(wallContent.getWallSubject())) {
                wallContent.setWallSubject(resolveContent.getContent());
            }
        }

        return wallContent;

    }

    private DiscoveryWallContent processDiscoveryWallContent(DiscoveryWallContent wallContent, WallBlockStyleConfig styleConfig) {
        ResolveContent resolveContent = new ResolveContent();
        resolveContent.setContent(wallContent.getWallContent());

        TextProcessor textProcessor = TextProcessorFatctory.get().getProcessorByKey(WordProcessorKey.KEY_DISCOVERY_CHAT);
        if (textProcessor != null) {
            resolveContent = textProcessor.process(resolveContent);
        }

        if (!StringUtil.isEmpty(wallContent.getWallSubject())) {
            String srcText = StringUtil.length(wallContent.getWallSubject()) <= styleConfig.getSubjectLen() ? wallContent.getWallSubject() : StringUtil.subString(wallContent.getWallSubject(), styleConfig.getSubjectLen()) + "...";
            wallContent.setWallSubject(srcText);
        }

        if (!StringUtil.isEmpty(wallContent.getWallContent())) {
            SubStringTextProcessor subStringTextProcessor = new SubStringTextProcessor(styleConfig.getContentLen(), "...", styleConfig.getBrLen());
            subStringTextProcessor.process(resolveContent);
            wallContent.setWallContent(resolveContent.getContent());
        }

        //process image
        if ((wallContent.getWallContentType().equals(WallContentType.IMAGE_H) || wallContent.getWallContentType().equals(WallContentType.IMAGE_V))
                && (wallContent.getWidth() != 0 && wallContent.getHeight() != 0)
                && wallContent.getWidth() > styleConfig.getWidth() && wallContent.getHeight() > styleConfig.getHeight()) {

            //按比例压缩
            if (wallContent.getWidth() * styleConfig.getHeight() >= styleConfig.getWidth() * wallContent.getHeight()) {
                //以height为准
                wallContent.setWidth(wallContent.getWidth() * styleConfig.getHeight() / wallContent.getHeight());
                wallContent.setHeight(styleConfig.getHeight());
            } else {
                //以width为准
                wallContent.setHeight(wallContent.getHeight() * styleConfig.getWidth() / wallContent.getWidth());
                wallContent.setWidth(styleConfig.getWidth());
            }
        }

        wallContent.setDateStr(DateUtil.parseDate(wallContent.getPublishDate()));

        return wallContent;
    }

}
