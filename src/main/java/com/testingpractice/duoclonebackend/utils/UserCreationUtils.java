package com.testingpractice.duoclonebackend.utils;

import java.util.concurrent.ThreadLocalRandom;

public class UserCreationUtils {

        private static final String BASE_URL = "https://storage.googleapis.com/duoclone-media/av";

        public static String getRandomProfilePic() {
            int num = ThreadLocalRandom.current().nextInt(2, 14);
            return BASE_URL + num + ".png";
        }

    public static String generateUsername(String name) {
        if (name == null) name = "user";
        String base = name.replaceAll("\\s+", "").toLowerCase();
        int rand = ThreadLocalRandom.current().nextInt(100, 1000);
        return base + rand;
    }

}
