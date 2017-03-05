package cn.com.helloclyde.ygoService.vo;

import cn.com.helloclyde.ygoService.mapper.model.YgodataWithBLOBs;

import java.util.Map;

/**
 * Created by HelloClyde on 2017/3/3.
 */
public class CardInfo{
    private int id;
    private String name;
    private String type;
    private String race;
    private String attribute;
    private int starNum;
    private int atk;
    private int def;
    private String depict;

    public CardInfo(Map<String,Object> ygodataMap){
        this.id = (int) ygodataMap.get("CardID");
        this.name = (String) ygodataMap.get("SCCardName");
        this.type = (String) ygodataMap.get("SCCardType");
        this.race = (String) ygodataMap.get("SCCardRace");
        this.attribute = (String) ygodataMap.get("SCCardAttribute");
        this.starNum = (int) ygodataMap.get("CardStarNum");
        this.atk = (int) ygodataMap.get("CardAtk");
        this.def = (int) ygodataMap.get("CardDef");
        this.depict = (String) ygodataMap.get("SCCardDepict");
    }

    public CardInfo(YgodataWithBLOBs ygodataWithBLOBs){
        this.id = ygodataWithBLOBs.getId() - 1;
        this.name = ygodataWithBLOBs.getSccardname();
        this.type = ygodataWithBLOBs.getSccardtype();
        this.race = ygodataWithBLOBs.getSccardrace();
        this.attribute = ygodataWithBLOBs.getSccardattribute();
        this.starNum = ygodataWithBLOBs.getCardstarnum();
        this.atk = ygodataWithBLOBs.getCardatk();
        this.def = ygodataWithBLOBs.getCarddef();
        this.depict = ygodataWithBLOBs.getSccarddepict();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public int getStarNum() {
        return starNum;
    }

    public void setStarNum(int starNum) {
        this.starNum = starNum;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = def;
    }

    public String getDepict() {
        return depict;
    }

    public void setDepict(String depict) {
        this.depict = depict;
    }

}
