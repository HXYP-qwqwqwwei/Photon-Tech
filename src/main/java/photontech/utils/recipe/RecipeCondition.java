package photontech.utils.recipe;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;

import static photontech.utils.PtConstants.*;


public class RecipeCondition {

    public float rate = -1;

    public float temperature = 0.0F;
    public int heat = 0;
    public float voltage = 0.0F;
    public int electron = 0;
    public int frequency = 0;
    public int photon = 0;

    public boolean hasTCondition = false;
    public boolean hasECondition = false;
    public boolean hasPCondition = false;

    public static RecipeCondition parse(JsonObject conditionObject) {
        if (conditionObject.isJsonNull()) {
            return new RecipeCondition();
        }
        return new RecipeCondition(conditionObject);
    }

    private RecipeCondition() {

    }

    private RecipeCondition(JsonObject conditionObject) {
        if (conditionObject.has(RATE)) {
            this.rate = JSONUtils.getAsFloat(conditionObject, RATE);
        }
        if (conditionObject.has(THERMAL_CONDITION)) {
            JsonObject thermal = JSONUtils.getAsJsonObject(conditionObject, THERMAL_CONDITION);
            this.temperature = JSONUtils.getAsFloat(thermal, TEMPERATURE);
            this.heat = JSONUtils.getAsInt(thermal, HEAT);
            this.hasTCondition = true;
        }
        if (conditionObject.has(ELECTRIC_CONDITION)) {
            JsonObject electric = JSONUtils.getAsJsonObject(conditionObject, ELECTRIC_CONDITION);
            this.voltage = JSONUtils.getAsFloat(electric, VOLTAGE);
            this.electron = JSONUtils.getAsInt(electric, ELECTRON);
            this.hasECondition = true;
        }
        if (conditionObject.has(PHOTOMETRY_CONDITION)) {
            JsonObject photometry = JSONUtils.getAsJsonObject(conditionObject, PHOTOMETRY_CONDITION);
            this.frequency = JSONUtils.getAsInt(photometry, FREQUENCY);
            this.photon = JSONUtils.getAsInt(photometry, PHOTON);
            this.hasPCondition = true;
        }
    }

    public void toNetwork(PacketBuffer packetBuffer) {
        packetBuffer.writeFloat(this.rate);
        packetBuffer.writeFloat(this.temperature);
        packetBuffer.writeInt(this.heat);
        packetBuffer.writeFloat(this.voltage);
        packetBuffer.writeInt(this.electron);
        packetBuffer.writeInt(this.frequency);
        packetBuffer.writeInt(this.photon);
    }

    public static RecipeCondition fromNetwork(PacketBuffer packetBuffer) {
        RecipeCondition condition = new RecipeCondition();
        condition.rate = packetBuffer.readFloat();
        condition.temperature = packetBuffer.readFloat();
        condition.heat = packetBuffer.readInt();
        condition.voltage = packetBuffer.readFloat();
        condition.electron = packetBuffer.readInt();
        condition.frequency = packetBuffer.readInt();
        condition.photon = packetBuffer.readInt();
        return condition;
    }

    public JsonObject toJSON() {
        JsonObject conditionObj = new JsonObject();

        if (this.rate >= 0) {
            conditionObj.addProperty(RATE, this.rate);
        }

        if (this.hasTCondition) {
            JsonObject thermalObj = new JsonObject();
            thermalObj.addProperty(TEMPERATURE, this.temperature);
            thermalObj.addProperty(HEAT, this.heat);
            conditionObj.add(THERMAL_CONDITION, thermalObj);
        }

        if (this.hasECondition) {
            JsonObject electricObj = new JsonObject();
            electricObj.addProperty(VOLTAGE, this.voltage);
            electricObj.addProperty(ELECTRON, this.electron);
            conditionObj.add(ELECTRIC_CONDITION, electricObj);
        }

        if (this.hasPCondition) {
            JsonObject photometryObj = new JsonObject();
            photometryObj.addProperty(FREQUENCY, this.frequency);
            photometryObj.addProperty(PHOTON, this.photon);
            conditionObj.add(PHOTOMETRY_CONDITION, photometryObj);
        }

        return conditionObj;
    }

    public RecipeCondition setTCondition(float temperature, int heat) {
        this.temperature = temperature;
        this.heat = heat;
        this.hasTCondition = true;
        return this;
    }

    public RecipeCondition setECondition(float voltage, int electron) {
        this.voltage = voltage;
        this.electron = electron;
        this.hasECondition = true;
        return this;
    }

    public RecipeCondition setPCondition(int frequency, int photon) {
        this.frequency = frequency;
        this.photon = photon;
        this.hasPCondition = true;
        return this;
    }

    public boolean testTCondition(RecipeCondition condition) {
        if (!this.hasTCondition) {
            return true;
        }
        return condition.temperature >= this.temperature;
    }

    public boolean testECondition(RecipeCondition condition) {
        if (!this.hasECondition) {
            return true;
        }
        return condition.voltage >= this.voltage;
    }

    public boolean testPCondition(RecipeCondition condition) {
        if (!this.hasPCondition) {
            return true;
        }
        return condition.frequency >= this.frequency;
    }

    public boolean testAllCondition(RecipeCondition condition) {
        return this.testTCondition(condition) && this.testECondition(condition) && this.testPCondition(condition);
    }

    public CompoundNBT writeToNBT(CompoundNBT nbt) {

        nbt.putFloat("Rate", this.rate);

        nbt.putBoolean("HasTCondition", this.hasTCondition);
        nbt.putBoolean("HasECondition", this.hasECondition);
        nbt.putBoolean("HasPCondition", this.hasPCondition);

        if (this.hasTCondition) {
            nbt.putFloat("Temperature", this.temperature);
            nbt.putInt("Heat", this.heat);
        }
        if (this.hasECondition) {
            nbt.putFloat("Voltage", this.voltage);
            nbt.putInt("Electron", this.electron);
        }
        if (this.hasPCondition) {
            nbt.putInt("Frequency", this.frequency);
            nbt.putInt("Photon", this.photon);
        }
        return nbt;
    }

    public static RecipeCondition loadFromNBT(CompoundNBT nbt) {
        RecipeCondition condition = new RecipeCondition();

        condition.rate = nbt.getFloat("Rate");

        if (nbt.getBoolean("HasTCondition")) {
            condition.setTCondition(nbt.getFloat("Temperature"), nbt.getInt("Heat"));
        }
        if (nbt.getBoolean("HasECondition")) {
            condition.setECondition(nbt.getFloat("Voltage"), nbt.getInt("Electron"));
        }
        if (nbt.getBoolean("HasPCondition")) {
            condition.setPCondition(nbt.getInt("Frequency"), nbt.getInt("Heat"));
        }
        return condition;
    }

}
