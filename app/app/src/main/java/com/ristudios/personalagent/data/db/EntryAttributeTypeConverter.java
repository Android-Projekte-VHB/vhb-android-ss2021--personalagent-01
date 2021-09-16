package com.ristudios.personalagent.data.db;
import androidx.room.TypeConverter;
import com.ristudios.personalagent.data.Category;
import com.ristudios.personalagent.data.Difficulty;

import java.util.UUID;

public class EntryAttributeTypeConverter {

    //TypeConverter for UUID <-> String
    @TypeConverter
    public static String uuidToString(UUID uuid) {
        return uuid == null ? null : uuid.toString();
    }
    //TypeConverter for String <-> UUID
    @TypeConverter
    public static UUID stringToUuid(String uuidString) {
        return uuidString == null ? null : UUID.fromString(uuidString);
    }
    //TypeConverter for category <-> integer
    @TypeConverter
    public static Integer categoryToInteger(Category category){
        return category.ordinal();
    }
    //TypeConverter for integer <-> category
    @TypeConverter
    public static Category integerToCategory(Integer categoryInteger) {
        return Category.values()[categoryInteger];
    }
    //TypeConverter for difficulty <-> integer
    @TypeConverter
    public static Integer difficultyToInteger(Difficulty difficulty){
        return difficulty.ordinal();
    }
    //TypeConverter for integer <-> difficulty
    @TypeConverter
    public static Difficulty integerToDifficulty(Integer difficultyInteger) {
        return Difficulty.values()[difficultyInteger];
    }

}
