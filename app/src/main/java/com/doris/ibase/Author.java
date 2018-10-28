package com.doris.ibase;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Doris on 2018/10/27.
 */
public class Author {


    /**
     * code : 200
     * message : 成功!
     * result : [{"name":"王维","desc":"王维，字摩诘，河东人。工书画，与弟缙俱有俊才。开元九年，进士擢第，调太乐丞，坐累为济州司仓参军。历右拾遗、监察御史、左补阙、库部郎中，拜吏部郎中。天宝末，为给事中。安禄山陷两都，维为贼所得，服药阳喑，拘于菩提寺。禄山宴凝碧池，维潜赋诗悲悼，闻于行在。赋平，陷贼官三等定罪，特原之。责授太子中允，迁中庶子、中书舍人，复拜给事中，转尚书右丞。维以诗名盛于开元、天宝间，宁薛诸王驸马豪贵之门，无不拂席迎之。得宋之问辋川别墅，山水绝胜，与道友裴迪，浮舟往来，弹琴赋诗，啸咏终日。笃于奉佛，晚年长斋禅诵。一日，忽索笔作书数纸，别弟缙及平生亲故，舍笔而卒，赠秘书监。宝应中，代宗问缙：\u201c朕常于诸王坐闻维乐章，今存几何？\u201d缙集诗六卷，文四卷，表上之。敕答云：\u201c卿伯氏位列先朝，名高希代，抗行周雅，长揖楚辞，诗家者流，时论归美，克成编录，叹息良深。\u201d殷璠谓：\u201c维诗词秀调雅，意新理惬，在泉成珠，著壁成绘。\u201d苏轼亦云：\u201c维诗中有画，画中有诗也。\u201d今编诗四卷。"}]
     */

    private int code;
    private String message;
    private List<ResultBean> result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * name : 王维
         * desc : 王维，字摩诘，河东人。工书画，与弟缙俱有俊才。开元九年，进士擢第，调太乐丞，坐累为济州司仓参军。历右拾遗、监察御史、左补阙、库部郎中，拜吏部郎中。天宝末，为给事中。安禄山陷两都，维为贼所得，服药阳喑，拘于菩提寺。禄山宴凝碧池，维潜赋诗悲悼，闻于行在。赋平，陷贼官三等定罪，特原之。责授太子中允，迁中庶子、中书舍人，复拜给事中，转尚书右丞。维以诗名盛于开元、天宝间，宁薛诸王驸马豪贵之门，无不拂席迎之。得宋之问辋川别墅，山水绝胜，与道友裴迪，浮舟往来，弹琴赋诗，啸咏终日。笃于奉佛，晚年长斋禅诵。一日，忽索笔作书数纸，别弟缙及平生亲故，舍笔而卒，赠秘书监。宝应中，代宗问缙：“朕常于诸王坐闻维乐章，今存几何？”缙集诗六卷，文四卷，表上之。敕答云：“卿伯氏位列先朝，名高希代，抗行周雅，长揖楚辞，诗家者流，时论归美，克成编录，叹息良深。”殷璠谓：“维诗词秀调雅，意新理惬，在泉成珠，著壁成绘。”苏轼亦云：“维诗中有画，画中有诗也。”今编诗四卷。
         */

        private String name;
        private String desc;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    public static Author getAuthor(String resultString){
        try {
            return new Gson().fromJson(resultString, Author.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
