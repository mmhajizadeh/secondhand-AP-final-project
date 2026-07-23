package com.secondhand.backend.config;

import com.secondhand.backend.entity.*;
import com.secondhand.backend.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(
            UserRepository userRepository,
            CityRepository cityRepository,
            CategoryRepository categoryRepository,
            AdvertisementRepository advertisementRepository,
            ConversationRepository conversationRepository,
            MessageRepository chatMessageRepository,
            RatingRepository ratingRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            if (userRepository.count() == 0) {
                User admin = new User();
                admin.setUsername("admin1");
                admin.setPassword(passwordEncoder.encode("123456"));
                admin.setFullName("Admin");
                admin.setPhone("09123456789");
                admin.setEmail("admin@ac.ir");
                admin.setRole(Role.ADMIN);
                admin.setStatus(UserStatus.ACTIVE);
                userRepository.save(admin);

                User user1 = new User();
                user1.setUsername("user1");
                user1.setPassword(passwordEncoder.encode("123456"));
                user1.setFullName("Mohammad Mahdi Hajizadeh");
                user1.setPhone("09999999999");
                user1.setEmail("test1@ac.ir");
                user1.setRole(Role.USER);
                user1.setStatus(UserStatus.ACTIVE);
                userRepository.save(user1);

                User user2 = new User();
                user2.setUsername("user2");
                user2.setPassword(passwordEncoder.encode("123456"));
                user2.setFullName("Amirhossein Hosseinpour");
                user2.setPhone("09000000000");
                user2.setEmail("test2@ac.ir");
                user2.setRole(Role.USER);
                user2.setStatus(UserStatus.ACTIVE);
                userRepository.save(user2);
            }

            if (cityRepository.count() == 0) {
                List<String> cityNames = List.of(
                        "آمل", "تهران", "تبریز", "ارومیه", "اردبیل", "اصفهان", "کرج", "ایلام",
                        "بندر بوشهر", "شهرکرد", "بیرجند", "مشهد", "بجنورد", "اهواز", "زنجان",
                        "سمنان", "زاهدان", "شیراز", "قزوین", "قم", "سنندج", "کرمان", "کرمانشاه",
                        "یاسوج", "گرگان", "رشت", "خرم‌آباد", "ساری", "اراک", "بندرعباس", "همدان", "یزد"
                );
                for (String cityName : cityNames) {
                    City city = new City();
                    city.setName(cityName);
                    cityRepository.save(city);
                }
            }

            if (categoryRepository.count() == 0) {
                List<String> categoryNames = List.of(
                        "کالای دیجیتال", "املاک و مسکن", "وسایل نقلیه", "لوازم خانگی",
                        "خدمات و تجهیزات", "وسایل شخصی", "سرگرمی و فراغت", "تجهیزات صنعتی و اداری"
                );
                for (String catName : categoryNames) {
                    Category category = new Category();
                    category.setName(catName);
                    categoryRepository.save(category);
                }
            }

            if (advertisementRepository.count() == 0) {
                City amol = cityRepository.findAll().stream().filter(c -> c.getName().equals("آمل")).findFirst().orElse(null);
                City tehran = cityRepository.findAll().stream().filter(c -> c.getName().equals("تهران")).findFirst().orElse(null);
                Category digital = categoryRepository.findAll().stream().filter(c -> c.getName().equals("کالای دیجیتال")).findFirst().orElse(null);
                Category homeApp = categoryRepository.findAll().stream().filter(c -> c.getName().equals("لوازم خانگی")).findFirst().orElse(null);

                User user1 = userRepository.findByUsername("user1").orElse(null);
                User user2 = userRepository.findByUsername("user2").orElse(null);

                // ACTIVE
                Advertisement ad1 = new Advertisement();
                ad1.setTitle("لپ‌تاپ گیمینگ ایسوس در حد نو");
                ad1.setDescription("لپ تاپ بسیار تمیز بدون خط و خش مناسب کارهای مهندسی و بازی");
                ad1.setPrice(95000000L);
                ad1.setStatus(AdvertisementStatus.ACTIVE);
                ad1.setCity(amol);
                ad1.setCategory(digital);
                ad1.setOwnerUsername(user1.getUsername());
                ad1.setOwnerId(user1.getId());
                advertisementRepository.save(ad1);

                // PENDING
                Advertisement ad2 = new Advertisement();
                ad2.setTitle("مکانیکال کیبورد ریدر");
                ad2.setDescription("کیبورد مکانیکی با نورپردازی RGB در حد نو");
                ad2.setPrice(2500000L);
                ad2.setStatus(AdvertisementStatus.PENDING);
                ad2.setCity(tehran);
                ad2.setCategory(digital);
                ad2.setOwnerUsername(user2.getUsername());
                ad2.setOwnerId(user2.getId());
                advertisementRepository.save(ad2);

                // SOLD
                Advertisement ad3 = new Advertisement();
                ad3.setTitle("میکسر و غذا ساز بوش");
                ad3.setDescription("غذا ساز بوش اصلاً استفاده نشده کاملا آکبند");
                ad3.setPrice(6800000L);
                ad3.setStatus(AdvertisementStatus.SOLD);
                ad3.setCity(amol);
                ad3.setCategory(homeApp);
                ad3.setOwnerUsername(user1.getUsername());
                ad3.setOwnerId(user1.getId());
                advertisementRepository.save(ad3);
            }
        };
    }
}