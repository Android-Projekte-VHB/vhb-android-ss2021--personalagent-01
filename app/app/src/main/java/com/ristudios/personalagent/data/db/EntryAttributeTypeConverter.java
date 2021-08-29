package com.ristudios.personalagent.data.db;
import androidx.room.TypeConverter;
import com.ristudios.personalagent.data.Category;
import com.ristudios.personalagent.data.Difficulty;

public class EntryAttributeTypeConverter {

    @TypeConverter
    public static Integer toCategoryInteger(Category category){
        return category.ordinal();
    }

    @TypeConverter
    public static Category toCategory(Integer categoryInteger) {
        return Category.values()[categoryInteger];
    }

    @TypeConverter
    public static Integer toDifficultyInteger(Difficulty difficulty){
        return difficulty.ordinal();
    }

    @TypeConverter
    public static Difficulty toDifficulty(Integer difficultyInteger) {
        return Difficulty.values()[difficultyInteger];
    }

}
