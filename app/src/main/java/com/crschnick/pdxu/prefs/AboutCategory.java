package com.crschnick.pdxu.prefs;

import com.crschnick.pdxu.comp.Comp;
import com.crschnick.pdxu.comp.base.LabelComp;
import com.crschnick.pdxu.comp.base.PrettyImageHelper;
import com.crschnick.pdxu.comp.base.VerticalComp;
import com.crschnick.pdxu.core.AppFontSizes;
import com.crschnick.pdxu.core.AppNames;
import com.crschnick.pdxu.core.AppProperties;
import com.crschnick.pdxu.platform.LabelGraphic;
import com.crschnick.pdxu.platform.OptionsBuilder;
import com.crschnick.pdxu.update.AppDistributionType;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import atlantafx.base.controls.Spacer;
import atlantafx.base.theme.Styles;

import java.util.List;

public class AboutCategory extends AppPrefsCategory {

    @Override
    public String getId() {
        return "about";
    }

    @Override
    protected LabelGraphic getIcon() {
        return new LabelGraphic.IconGraphic("mdi2i-information-outline");
    }

    @Override
    public Comp<?> create() {
        var props = createProperties();
        var update = new UpdateCheckComp().prefWidth(600);
        return new VerticalComp(List.of(
                        props,
                        Comp.vspacer(1),
                        update,
                        Comp.vspacer(5),
                        Comp.hseparator().padding(Insets.EMPTY).maxWidth(600)))
                .apply(s -> s.get().setFillWidth(true))
                .apply(struc -> struc.get().setSpacing(12))
                .styleClass("information")
                .styleClass("about-tab");
    }

    private Comp<?> createProperties() {
        var title = Comp.of(() -> {
            var header = new Label();
            header.setText(AppNames.ofCurrent().getName() + " Desktop");
            AppFontSizes.xl(header);
            var desc = new Label();
            desc.setText("Version " + AppProperties.get().getVersion() + " ("
                    + AppProperties.get().getArch() + ")");
            AppFontSizes.xs(desc);
            var text = new VBox(header, new Spacer(), desc);
            text.setAlignment(Pos.CENTER_LEFT);

            var size = 40;
            var graphic =
                    PrettyImageHelper.ofFixedSizeSquare("logo/logo.png", size).createRegion();

            var hbox = new HBox(graphic, text);
            hbox.setAlignment(Pos.CENTER_LEFT);
            hbox.setFillHeight(true);
            hbox.setSpacing(10);
            return hbox;
        });

        title.styleClass(Styles.TEXT_BOLD);

        var section = new OptionsBuilder()
                .addComp(Comp.vspacer(40))
                .addComp(title, null)
                .addComp(Comp.vspacer(10))
                .name("build")
                .addComp(new LabelComp(AppProperties.get().getBuild()), null)
                .name("distribution")
                .addComp(new LabelComp(AppDistributionType.get().toTranslatedString()))
                .name("virtualMachine")
                .addComp(
                        new LabelComp(System.getProperty("java.vm.vendor") + " "
                                + System.getProperty("java.vm.name")
                                + " "
                                + System.getProperty("java.vm.version")),
                        null)
                .buildComp();
        return section.styleClass("properties-comp");
    }
}
