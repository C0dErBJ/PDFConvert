package com.zjl.pdfconvert.model.word;

/**
 * @author Zhu jialiang
 * @date 2020/8/28
 */
public enum FontName {

    PMingLiU("PMingLiU", "新细明体"),
    MingLiU("MingLiU", "细明体"),
    DFKai("DFKai-SB", "标楷体"),
    SimHei("SimHei", "黑体"),
    SimSun("SimSun", "宋体"),
    NSimSun("NSimSun", "新宋体"),
    FangSong("FangSong", "仿宋"),
    KaiTi("KaiTi", "楷体"),
    FangSong_GB2312("FangSong_GB2312", "仿宋_GB2312"),
    KaiTi_GB2312("KaiTi_GB2312", "楷体_GB2312"),
    JhengHei("Microsoft JhengHei", "微软正黑体"),
    YaHei("Microsoft YaHei", "微软雅黑");

    private String zhName;
    private String enName;

    FontName(String enName, String zhName) {
        this.enName = enName;
        this.zhName = zhName;
    }

    public String getName() {
        return zhName;
    }

    public String getEnName() {
        return enName;
    }

    public static String getEnName(String zhName) {
        for (FontName fontName : FontName.values()) {
            if (fontName.zhName.equals(zhName)) {
                return fontName.enName;
            }
        }
        return SimSun.enName;
    }

    public static String getName(String enName) {
        for (FontName fontName : FontName.values()) {
            if (fontName.enName.equals(enName)) {
                return fontName.zhName;
            }
        }
        return SimSun.zhName;
    }

    public static FontName fromEnName(String name) {
        for (FontName fontName : FontName.values()) {
            if (fontName.enName.equals(name)) {
                return fontName;
            }
        }
        return SimSun;
    }

    public static FontName fromZhName(String name) {
        for (FontName fontName : FontName.values()) {
            if (fontName.enName.equals(name)) {
                return fontName;
            }
        }
        return SimSun;
    }

}
