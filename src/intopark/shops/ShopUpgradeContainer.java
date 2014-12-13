/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.shops;

/**
 *
 * @author arska
 */
public class ShopUpgradeContainer {
    private boolean cleaningUpgrade=false;
    private boolean friendlyStaffUpgrade=false;
    private boolean qualityUpgrade=false;
    private boolean trendyUpgrade=false;

    private int cleaningUpgradeCost=75;
    private int friendlyStaffUpgradeCost=100;
    private int qualityUpgradeCost=200;
    private int trendyUpgradeCost=125;

    private ShopReputation reputation= ShopReputation.NEW;

    public ShopUpgradeContainer() {
    }

    public void setTrendyUpgradeCost(int trendyUpgradeCost) {
        this.trendyUpgradeCost = trendyUpgradeCost;
    }

    public void setTrendyUpgrade(boolean trendyUpgrade) {
        this.trendyUpgrade = trendyUpgrade;
    }

    public boolean isTrendyUpgrade() {
        return trendyUpgrade;
    }

    public int getTrendyUpgradeCost() {
        return trendyUpgradeCost;
    }

    public ShopReputation getReputation() {
        return reputation;
    }

    public void setReputation(ShopReputation reputation) {
        this.reputation = reputation;
    }

    public boolean isCleaningUpgrade() {
        return cleaningUpgrade;
    }

    public boolean isFriendlyStaffUpgrade() {
        return friendlyStaffUpgrade;
    }

    public boolean isQualityUpgrade() {
        return qualityUpgrade;
    }

    public void setCleaningUpgrade(boolean cleaningUpgrade) {
        this.cleaningUpgrade = cleaningUpgrade;
    }

    public void setFriendlyStaffUpgrade(boolean friendlyStaffUpgrade) {
        this.friendlyStaffUpgrade = friendlyStaffUpgrade;
    }

    public void setQualityUpgrade(boolean qualityUpgrade) {
        this.qualityUpgrade = qualityUpgrade;
    }

    public int getCleaningUpgradeCost() {
        return cleaningUpgradeCost;
    }

    public int getFriendlyStaffUpgradeCost() {
        return friendlyStaffUpgradeCost;
    }

    public int getQualityUpgradeCost() {
        return qualityUpgradeCost;
    }

    public void setCleaningUpgradeCost(int cleaningUpgradeCost) {
        this.cleaningUpgradeCost = cleaningUpgradeCost;
    }

    public void setFriendlyStaffUpgradeCost(int friendlyStaffUpgradeCost) {
        this.friendlyStaffUpgradeCost = friendlyStaffUpgradeCost;
    }

    public void setQualityUpgradeCost(int qualityUpgradeCost) {
        this.qualityUpgradeCost = qualityUpgradeCost;
    }

}
