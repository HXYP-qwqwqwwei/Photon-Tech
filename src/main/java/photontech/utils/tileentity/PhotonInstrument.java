package photontech.utils.tileentity;

import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.function.Predicate;

public interface PhotonInstrument {

    enum PhotonType {

        MICROWAVE   (0),
        INFRARED    (1),
        RED         (2),
        GREEN       (3),
        BLUE        (4),
        ULTRAVIOLET (5),
        X_RAY       (6),
        GAMMA_RAY   (7);

        final int frequencyLevel;

        PhotonType(int frequencyLevel) {
            this.frequencyLevel = frequencyLevel;
        }

        public static PhotonType valueOf(int value) {
            switch (value) {
                case 0: return MICROWAVE;
                case 1: return INFRARED;
                case 2: return RED;
                case 3: return GREEN;
                case 4: return BLUE;
                case 5: return ULTRAVIOLET;
                case 6: return X_RAY;
                case 7: return GAMMA_RAY;
            }
            return RED;
        }
    }

    class PhotonPack {
        private final int[] photonsWithLevel = new int[8];

        public PhotonPack(int[] photonsWithLevelIn) {
            int copyLength = Math.min(this.photonsWithLevel.length, photonsWithLevelIn.length);
            System.arraycopy(photonsWithLevelIn, 0, this.photonsWithLevel, 0, copyLength);
        }

        public int extractEnergy(Predicate<Integer> validator) {
            int energy = 0;
            for (int level = 0; level < 8; ++level) {
                if (validator.test(level)) {
                    energy += (level + 1) * photonsWithLevel[level];
                    photonsWithLevel[level] = 0;
                }
            }
            return energy;
        }

        public boolean attenuation(float[] rate) {
            int minLen = Math.min(rate.length, photonsWithLevel.length);
            for (int i = 0; i < minLen; ++i) {
                photonsWithLevel[i] = (int) (photonsWithLevel[i] * rate[i]);
            }
            for (int i = minLen; i < photonsWithLevel.length; ++i) {
                photonsWithLevel[i] = 0;
            }
            return Arrays.stream(photonsWithLevel).max().orElse(0) > 0;
        }

    }

    int[] SKY_LIGHT = {
            0, 0, 5, 5, 5
    };

    void acceptPhotonPackFrom(@Nonnull PhotonPack pack, @Nullable Vector3d injectionVector);

    void radiatePhotonPackTo(@Nonnull PhotonPack pack, @Nullable Vector3d ejectionVector);


}
