package photontech.utils.recipe;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;

import static photontech.utils.PtConstants.*;


public class RecipeCondition {
    public static final RecipeCondition IMPOSSIBLE = new RecipeCondition();

    // 温度条件，必填
    public float temperature = -1.0F;
    public int heat = 1000;
    // 通电条件，可选
    public float voltage = 0.0F;
    public int electron = 0;
    // 光照条件，可选
    public int frequency = 0;
    public int photon = 0;

    public static RecipeCondition parse(JsonObject conditionObject) {
        if (conditionObject.isJsonNull()) {
            return IMPOSSIBLE;
        }
        return new RecipeCondition(conditionObject);
    }

    private RecipeCondition() {

    }

    private RecipeCondition(JsonObject conditionObject) {
        if (conditionObject.has(THERMAL_CONDITION)) {
            JsonObject thermal = JSONUtils.getAsJsonObject(conditionObject, THERMAL_CONDITION);
            this.temperature = JSONUtils.getAsFloat(thermal, TEMPERATURE);
            this.heat = JSONUtils.getAsInt(thermal, HEAT);
        }
        if (conditionObject.has(ELECTRIC_CONDITION)) {
            JsonObject electric = JSONUtils.getAsJsonObject(conditionObject, ELECTRIC_CONDITION);
            this.voltage = JSONUtils.getAsFloat(electric, VOLTAGE);
            this.electron = JSONUtils.getAsInt(electric, ELECTRON);
        }
        if (conditionObject.has(PHOTOMETRY_CONDITION)) {
            JsonObject photometry = JSONUtils.getAsJsonObject(conditionObject, PHOTOMETRY_CONDITION);
            this.frequency = JSONUtils.getAsInt(photometry, FREQUENCY);
            this.photon = JSONUtils.getAsInt(photometry, PHOTON);
        }
    }

    public void toNetwork(PacketBuffer packetBuffer) {
        packetBuffer.writeFloat(this.temperature);
        packetBuffer.writeInt(this.heat);
        packetBuffer.writeFloat(this.voltage);
        packetBuffer.writeInt(this.electron);
        packetBuffer.writeInt(this.frequency);
        packetBuffer.writeInt(this.photon);
    }

    public static RecipeCondition fromNetwork(PacketBuffer packetBuffer) {
        RecipeCondition condition = new RecipeCondition();
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

        JsonObject thermalObj = new JsonObject();
        thermalObj.addProperty(TEMPERATURE, this.temperature);
        thermalObj.addProperty(HEAT, this.heat);
        conditionObj.add(THERMAL_CONDITION, thermalObj);

        if (this.electron != 0) {
            JsonObject electricObj = new JsonObject();
            electricObj.addProperty(VOLTAGE, this.voltage);
            electricObj.addProperty(ELECTRON, this.electron);
            conditionObj.add(ELECTRIC_CONDITION, electricObj);
        }

        if (this.photon > 0) {
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
        return this;
    }

    public RecipeCondition setECondition(float voltage, int electron) {
        this.voltage = voltage;
        this.electron = electron;
        return this;
    }

    public RecipeCondition setPCondition(int frequency, int photon) {
        this.frequency = frequency;
        this.photon = photon;
        return this;
    }

    /**
     * 测试温度条件，如果是吸热反应，则条件的温度是最低温度，若是放热反应，则条件的温度是最高温度
     * @param condition 输入的条件
     * @return 是否满足温度条件
     */
    public boolean testTCondition(RecipeCondition condition) {
        if (this.heat > 0) {
            return condition.temperature >= this.temperature;
        }
        else if (this.heat < 0) {
            return condition.temperature <= this.temperature;
        }
        return true;
    }

    public boolean testECondition(RecipeCondition condition) {
        if (this.voltage < 0) {
            return condition.voltage < this.voltage;
        }
        else if (this.voltage > 0) {
            return condition.voltage > this.voltage;
        }
        return true;
    }

    public boolean testPCondition(RecipeCondition condition) {
        if (this.photon <= 0) {
            return true;
        }
        return condition.frequency >= this.frequency;
    }

    public boolean testAllCondition(RecipeCondition condition) {
        return this.testTCondition(condition) && this.testECondition(condition) && this.testPCondition(condition);
    }

    public CompoundNBT writeToNBT(CompoundNBT nbt) {

        nbt.putFloat("Temperature", this.temperature);
        nbt.putInt("Heat", this.heat);

        if (this.electron != 0) {
            nbt.putFloat("Voltage", this.voltage);
            nbt.putInt("Electron", this.electron);
        }
        if (this.photon > 0) {
            nbt.putInt("Frequency", this.frequency);
            nbt.putInt("Photon", this.photon);
        }
        return nbt;
    }

    public static RecipeCondition loadFromNBT(CompoundNBT nbt) {
        RecipeCondition condition = new RecipeCondition();
        condition.setTCondition(nbt.getFloat("Temperature"), nbt.getInt("Heat"));
        condition.setECondition(nbt.getFloat("Voltage"), nbt.getInt("Electron"));
        condition.setPCondition(nbt.getInt("Frequency"), nbt.getInt("Heat"));
        return condition;
    }

}
