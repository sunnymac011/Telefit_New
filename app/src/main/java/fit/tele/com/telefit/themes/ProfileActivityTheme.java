package fit.tele.com.telefit.themes;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.databinding.ActivityProfileBinding;
import fit.tele.com.telefit.utils.Preferences;

public class ProfileActivityTheme {

    Preferences preferences;
    TextView txt_food,txt_meals,txt_recipes,txt_exercises,txt_trainer,txt_app_preferences,txt_themes,txt_units,txt_country
            ,txt_device,txt_privacy,txt_notifications,txt_about,txt_help;

    public void setTheme(ActivityProfileBinding binding, Activity activity) {
        preferences = new Preferences(activity);
        txt_food = (TextView) activity.findViewById(R.id.txt_food);
        txt_meals = (TextView) activity.findViewById(R.id.txt_meals);
        txt_recipes = (TextView) activity.findViewById(R.id.txt_recipes);
        txt_exercises = (TextView) activity.findViewById(R.id.txt_exercises);
        txt_trainer = (TextView) activity.findViewById(R.id.txt_trainer);
        txt_app_preferences = (TextView) activity.findViewById(R.id.txt_app_preferences);
        txt_themes = (TextView) activity.findViewById(R.id.txt_themes);
        txt_units = (TextView) activity.findViewById(R.id.txt_units);
        txt_country = (TextView) activity.findViewById(R.id.txt_country);
        txt_device = (TextView) activity.findViewById(R.id.txt_device);
        txt_privacy = (TextView) activity.findViewById(R.id.txt_privacy);
        txt_notifications = (TextView) activity.findViewById(R.id.txt_notifications);
        txt_about = (TextView) activity.findViewById(R.id.txt_about);
        txt_help = (TextView) activity.findViewById(R.id.txt_help);

        if (preferences.getTheme().toString().equalsIgnoreCase("Default")) {
            binding.rlHeader.setBackgroundColor(activity.getResources().getColor(R.color.main_color));
            txt_food.setTextColor(activity.getResources().getColor(R.color.main_color));
            txt_meals.setTextColor(activity.getResources().getColor(R.color.main_color));
            txt_recipes.setTextColor(activity.getResources().getColor(R.color.main_color));
            txt_exercises.setTextColor(activity.getResources().getColor(R.color.main_color));
            txt_trainer.setTextColor(activity.getResources().getColor(R.color.main_color));
            txt_app_preferences.setTextColor(activity.getResources().getColor(R.color.main_color));
            txt_themes.setTextColor(activity.getResources().getColor(R.color.main_color));
            txt_units.setTextColor(activity.getResources().getColor(R.color.main_color));
            txt_country.setTextColor(activity.getResources().getColor(R.color.main_color));
            txt_device.setTextColor(activity.getResources().getColor(R.color.main_color));
            txt_privacy.setTextColor(activity.getResources().getColor(R.color.main_color));
            txt_notifications.setTextColor(activity.getResources().getColor(R.color.main_color));
            txt_about.setTextColor(activity.getResources().getColor(R.color.main_color));
            txt_help.setTextColor(activity.getResources().getColor(R.color.main_color));
        }
        else if (preferences.getTheme().toString().equalsIgnoreCase("Light Blue")) {
            binding.rlHeader.setBackgroundColor(activity.getResources().getColor(R.color.yellow));
            txt_food.setTextColor(activity.getResources().getColor(R.color.light_blue_text));
            txt_meals.setTextColor(activity.getResources().getColor(R.color.light_blue_text));
            txt_recipes.setTextColor(activity.getResources().getColor(R.color.light_blue_text));
            txt_exercises.setTextColor(activity.getResources().getColor(R.color.light_blue_text));
            txt_trainer.setTextColor(activity.getResources().getColor(R.color.light_blue_text));
            txt_app_preferences.setTextColor(activity.getResources().getColor(R.color.light_blue_text));
            txt_themes.setTextColor(activity.getResources().getColor(R.color.light_blue_text));
            txt_units.setTextColor(activity.getResources().getColor(R.color.light_blue_text));
            txt_country.setTextColor(activity.getResources().getColor(R.color.light_blue_text));
            txt_device.setTextColor(activity.getResources().getColor(R.color.light_blue_text));
            txt_privacy.setTextColor(activity.getResources().getColor(R.color.light_blue_text));
            txt_notifications.setTextColor(activity.getResources().getColor(R.color.light_blue_text));
            txt_about.setTextColor(activity.getResources().getColor(R.color.light_blue_text));
            txt_help.setTextColor(activity.getResources().getColor(R.color.light_blue_text));
        }
        else if (preferences.getTheme().toString().equalsIgnoreCase("Yellow")) {
            binding.rlHeader.setBackgroundColor(activity.getResources().getColor(R.color.yellow));
            txt_food.setTextColor(activity.getResources().getColor(R.color.yellow));
            txt_meals.setTextColor(activity.getResources().getColor(R.color.yellow));
            txt_recipes.setTextColor(activity.getResources().getColor(R.color.yellow));
            txt_exercises.setTextColor(activity.getResources().getColor(R.color.yellow));
            txt_trainer.setTextColor(activity.getResources().getColor(R.color.yellow));
            txt_app_preferences.setTextColor(activity.getResources().getColor(R.color.yellow));
            txt_themes.setTextColor(activity.getResources().getColor(R.color.yellow));
            txt_units.setTextColor(activity.getResources().getColor(R.color.yellow));
            txt_country.setTextColor(activity.getResources().getColor(R.color.yellow));
            txt_device.setTextColor(activity.getResources().getColor(R.color.yellow));
            txt_privacy.setTextColor(activity.getResources().getColor(R.color.yellow));
            txt_notifications.setTextColor(activity.getResources().getColor(R.color.yellow));
            txt_about.setTextColor(activity.getResources().getColor(R.color.yellow));
            txt_help.setTextColor(activity.getResources().getColor(R.color.yellow));
        }
        else if (preferences.getTheme().toString().equalsIgnoreCase("Purple")) {
            binding.rlHeader.setBackgroundColor(activity.getResources().getColor(R.color.purple));
            txt_food.setTextColor(activity.getResources().getColor(R.color.purple));
            txt_meals.setTextColor(activity.getResources().getColor(R.color.purple));
            txt_recipes.setTextColor(activity.getResources().getColor(R.color.purple));
            txt_exercises.setTextColor(activity.getResources().getColor(R.color.purple));
            txt_trainer.setTextColor(activity.getResources().getColor(R.color.purple));
            txt_app_preferences.setTextColor(activity.getResources().getColor(R.color.purple));
            txt_themes.setTextColor(activity.getResources().getColor(R.color.purple));
            txt_units.setTextColor(activity.getResources().getColor(R.color.purple));
            txt_country.setTextColor(activity.getResources().getColor(R.color.purple));
            txt_device.setTextColor(activity.getResources().getColor(R.color.purple));
            txt_privacy.setTextColor(activity.getResources().getColor(R.color.purple));
            txt_notifications.setTextColor(activity.getResources().getColor(R.color.purple));
            txt_about.setTextColor(activity.getResources().getColor(R.color.purple));
            txt_help.setTextColor(activity.getResources().getColor(R.color.purple));
        }
        else if (preferences.getTheme().toString().equalsIgnoreCase("Red")) {
            binding.rlHeader.setBackgroundColor(activity.getResources().getColor(R.color.red));
            txt_food.setTextColor(activity.getResources().getColor(R.color.red));
            txt_meals.setTextColor(activity.getResources().getColor(R.color.red));
            txt_recipes.setTextColor(activity.getResources().getColor(R.color.red));
            txt_exercises.setTextColor(activity.getResources().getColor(R.color.red));
            txt_trainer.setTextColor(activity.getResources().getColor(R.color.red));
            txt_app_preferences.setTextColor(activity.getResources().getColor(R.color.red));
            txt_themes.setTextColor(activity.getResources().getColor(R.color.red));
            txt_units.setTextColor(activity.getResources().getColor(R.color.red));
            txt_country.setTextColor(activity.getResources().getColor(R.color.red));
            txt_device.setTextColor(activity.getResources().getColor(R.color.red));
            txt_privacy.setTextColor(activity.getResources().getColor(R.color.red));
            txt_notifications.setTextColor(activity.getResources().getColor(R.color.red));
            txt_about.setTextColor(activity.getResources().getColor(R.color.red));
            txt_help.setTextColor(activity.getResources().getColor(R.color.red));
        }
        else if (preferences.getTheme().toString().equalsIgnoreCase("Blue")) {
            binding.rlHeader.setBackgroundColor(activity.getResources().getColor(R.color.blue));
            txt_food.setTextColor(activity.getResources().getColor(R.color.blue));
            txt_meals.setTextColor(activity.getResources().getColor(R.color.blue));
            txt_recipes.setTextColor(activity.getResources().getColor(R.color.blue));
            txt_exercises.setTextColor(activity.getResources().getColor(R.color.blue));
            txt_trainer.setTextColor(activity.getResources().getColor(R.color.blue));
            txt_app_preferences.setTextColor(activity.getResources().getColor(R.color.blue));
            txt_themes.setTextColor(activity.getResources().getColor(R.color.blue));
            txt_units.setTextColor(activity.getResources().getColor(R.color.blue));
            txt_country.setTextColor(activity.getResources().getColor(R.color.blue));
            txt_device.setTextColor(activity.getResources().getColor(R.color.blue));
            txt_privacy.setTextColor(activity.getResources().getColor(R.color.blue));
            txt_notifications.setTextColor(activity.getResources().getColor(R.color.blue));
            txt_about.setTextColor(activity.getResources().getColor(R.color.blue));
            txt_help.setTextColor(activity.getResources().getColor(R.color.blue));
        }
    }
}
