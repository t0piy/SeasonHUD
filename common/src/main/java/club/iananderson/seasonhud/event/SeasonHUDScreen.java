package club.iananderson.seasonhud.event;


import club.iananderson.seasonhud.config.Config;
import club.iananderson.seasonhud.config.Location;
import club.iananderson.seasonhud.config.ShowDay;
import club.iananderson.seasonhud.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import static club.iananderson.seasonhud.config.Config.*;

public class SeasonHUDScreen extends Screen{
    //private static final int COLUMNS = 2;
    private static final int MENU_PADDING_FULL = 50;
    private static final int MENU_PADDING_HALF = MENU_PADDING_FULL/2;
    private static final int PADDING = 4;
    private static final int BUTTON_WIDTH_FULL = 200;
    private static final int BUTTON_WIDTH_HALF = 180;
    private static final int BUTTON_HEIGHT = 20;
    public static Screen seasonScreen;

    private final Screen lastScreen;

    private static final Component TITLE = Component.translatable("menu.seasonhud.title");
    private static final Component JOURNEYMAP = Component.translatable("menu.seasonhud.journeymap");


    public SeasonHUDScreen(Screen seasonScreen){
        super(TITLE);
        this.lastScreen = seasonScreen;
    }
    public boolean isPauseScreen() {
        return true;
    }

    @Override
    public void render(@NotNull GuiGraphics stack, int mouseX, int mouseY, float partialTicks){
        this.renderDirtBackground(stack);
        stack.drawCenteredString(font, TITLE, this.width / 2, PADDING, 16777215);
        if(Services.PLATFORM.isModLoaded("journeymap")) {
            stack.drawCenteredString(font, JOURNEYMAP, this.width / 2, MENU_PADDING_FULL + (5 * (BUTTON_HEIGHT + PADDING)), 16777215);
        }
        super.render(stack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void init() {
        super.init();
        Minecraft mc = Minecraft.getInstance();

        int BUTTON_START_X_LEFT = (this.width / 2) - (BUTTON_WIDTH_HALF + PADDING);
        int BUTTON_START_X_RIGHT = (this.width / 2) + PADDING;
        int BUTTON_START_Y = MENU_PADDING_FULL;
        int y_OFFSET = BUTTON_HEIGHT + PADDING;

        //Buttons

        int row = 0;
        CycleButton<Boolean> enableModButton = CycleButton.onOffBuilder(enableMod.get())
                .create(BUTTON_START_X_LEFT, (BUTTON_START_Y + (row * y_OFFSET)), BUTTON_WIDTH_HALF, BUTTON_HEIGHT,
                        Component.translatable("menu.seasonhud.button.enableMod"),
                        (b, Off) -> Config.setEnableMod(Off));

        CycleButton<Location> hudLocationButton = CycleButton.builder(Location::getLocationName)
                .withValues(Location.TOP_LEFT, Location.TOP_CENTER, Location.TOP_RIGHT, Location.BOTTOM_LEFT, Location.BOTTOM_RIGHT)
                .withInitialValue(hudLocation.get())
                .create(BUTTON_START_X_RIGHT, (BUTTON_START_Y + (row * y_OFFSET)), BUTTON_WIDTH_HALF, BUTTON_HEIGHT,
                        Component.translatable("menu.seasonhud.button.hudLocation"),
                        (b, location) -> Config.setHudLocation(location));

        row = 1;
        CycleButton<Boolean> showTropicalSeasonButton = CycleButton.onOffBuilder(showTropicalSeason.get())
                .create(BUTTON_START_X_LEFT, (BUTTON_START_Y + (row * y_OFFSET)), BUTTON_WIDTH_HALF, BUTTON_HEIGHT,
                        Component.translatable("menu.seasonhud.button.showTropicalSeason"),
                        (b, Off) -> Config.setShowTropicalSeason(Off));

        CycleButton<Boolean> showSubSeasonButton = CycleButton.onOffBuilder(showSubSeason.get())
                .create(BUTTON_START_X_RIGHT, (BUTTON_START_Y + (row * y_OFFSET)), BUTTON_WIDTH_HALF, BUTTON_HEIGHT,
                        Component.translatable("menu.seasonhud.button.showSubSeason"),
                        (b, Off) -> Config.setShowSubSeason(Off));

        row = 2;
        ShowDay[] showDayValuesForge = { ShowDay.NONE,ShowDay.SHOW_DAY,ShowDay.SHOW_WITH_TOTAL_DAYS };
        ShowDay[] showDayValuesFabric = { ShowDay.NONE,ShowDay.SHOW_DAY,ShowDay.SHOW_WITH_TOTAL_DAYS,ShowDay.SHOW_WITH_MONTH };
        boolean isForge = Services.PLATFORM.getPlatformName() == "Forge";
        CycleButton<ShowDay> showDayButton = CycleButton.builder(ShowDay::getDayDisplayName)
                .withValues(isForge ? showDayValuesForge : showDayValuesFabric)
                .withInitialValue(showDay.get())
                .create(BUTTON_START_X_LEFT, (BUTTON_START_Y + (row * y_OFFSET)), BUTTON_WIDTH_HALF, BUTTON_HEIGHT,
                        Component.translatable("menu.seasonhud.button.showDay"),
                        (b, showDay) -> Config.setShowDay(showDay));


        CycleButton<Boolean> needCalendarButton = CycleButton.onOffBuilder(needCalendar.get())
                .create(BUTTON_START_X_RIGHT, (BUTTON_START_Y + (row * y_OFFSET)), BUTTON_WIDTH_HALF, BUTTON_HEIGHT,
                        Component.translatable("menu.seasonhud.button.needCalendar"),
                        (b, Off) -> Config.setNeedCalendar(Off));

        row = 3;
        CycleButton<Boolean> enableMinimapIntegrationButton = CycleButton.onOffBuilder(enableMinimapIntegration.get())
                .create(BUTTON_START_X_LEFT, (BUTTON_START_Y + (row * y_OFFSET)), BUTTON_WIDTH_HALF, BUTTON_HEIGHT,
                        Component.translatable("menu.seasonhud.button.enableMinimapIntegration"),
                        (b, Off) -> Config.setEnableMinimapIntegration(Off));

        CycleButton<Boolean> showMinimapHiddenButton = CycleButton.onOffBuilder(showMinimapHidden.get())
                .create(BUTTON_START_X_RIGHT, (BUTTON_START_Y + (row * y_OFFSET)), BUTTON_WIDTH_HALF, BUTTON_HEIGHT,
                        Component.translatable("menu.seasonhud.button.showMinimapHidden"),
                        (b, Off) -> Config.setShowMinimapHidden(Off));


        if(Services.PLATFORM.isModLoaded("journeymap")) {
            row = 6;
            CycleButton<Boolean> journeyMapAboveMapButton = CycleButton.onOffBuilder(journeyMapAboveMap.get())
                    .create(BUTTON_START_X_LEFT, (BUTTON_START_Y + (row * y_OFFSET)), BUTTON_WIDTH_HALF, BUTTON_HEIGHT,
                            Component.translatable("menu.seasonhud.button.journeyMapAboveMap"),
                            (b, Off) -> Config.setJourneyMapAboveMap(Off));
            addRenderableWidget(journeyMapAboveMapButton);
        }
        if(Services.PLATFORM.isModLoaded("journeymap")) {
            row = 6;
            CycleButton<Boolean> journeyMapMacOSButton = CycleButton.onOffBuilder(journeyMapMacOS.get())
                    .create(BUTTON_START_X_RIGHT, (BUTTON_START_Y + (row * y_OFFSET)), BUTTON_WIDTH_HALF, BUTTON_HEIGHT,
                            Component.translatable("menu.seasonhud.button.journeyMapMacOS"),
                            (b, Off) -> Config.setJourneyMapMacOS(Off));
            addRenderableWidget(journeyMapMacOSButton);
        }

        Button doneButton = Button.builder(Component.translatable("gui.done"), button -> {
                    mc.options.save();
                    mc.setScreen(this.lastScreen);
                })
                .bounds(this.width / 2 - (BUTTON_WIDTH_FULL / 2), (this.height - BUTTON_HEIGHT - PADDING),BUTTON_WIDTH_FULL, BUTTON_HEIGHT)
                .build();

        addRenderableWidget(enableModButton);
        addRenderableWidget(hudLocationButton);
        addRenderableWidget(showTropicalSeasonButton);
        addRenderableWidget(showSubSeasonButton);
        addRenderableWidget(showDayButton);
        addRenderableWidget(needCalendarButton);
        addRenderableWidget(enableMinimapIntegrationButton);
        addRenderableWidget(showMinimapHiddenButton);


        addRenderableWidget(doneButton);
    }
    public static void open(){
        Minecraft.getInstance().setScreen(new SeasonHUDScreen(seasonScreen));
    }
}
