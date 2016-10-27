package com.gluonhq.comments20;

import com.airhacks.afterburner.injection.Injector;
import com.gluonhq.charm.down.Platform;
import com.gluonhq.charm.down.Services;
import com.gluonhq.charm.down.plugins.DisplayService;
import com.gluonhq.charm.down.plugins.LifecycleService;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.application.ViewStackPolicy;
import com.gluonhq.charm.glisten.control.Avatar;
import com.gluonhq.charm.glisten.control.NavigationDrawer;
import com.gluonhq.charm.glisten.control.NavigationDrawer.Item;
import com.gluonhq.charm.glisten.control.NavigationDrawer.ViewItem;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import static com.gluonhq.comments20.Comments20.COMMENTS_VIEW;
import static com.gluonhq.comments20.Comments20.EDITION_VIEW;
import static com.gluonhq.comments20.Comments20.MENU_LAYER;
import com.gluonhq.comments20.cloud.Service;
import javafx.scene.Node;
import javafx.scene.image.Image;

public class DrawerManager {

    private final Service service;
    
    private final NavigationDrawer drawer;
    private final Avatar avatar;
    
    public DrawerManager() {
        this.drawer = new NavigationDrawer();

        service = Injector.instantiateModelOrService(Service.class);
        
        avatar = new Avatar(21, new Image(DrawerManager.class.getResourceAsStream("/icon.png")));
        Services.get(DisplayService.class)
                .ifPresent(d -> {
                    if (d.isTablet()) {
                        avatar.getStyleClass().add("tablet");
                    }
                });
        service.userProperty().addListener((obs, ov, nv) -> avatar.setImage(getAvatarImage()));
        avatar.setImage(getAvatarImage());
                
        NavigationDrawer.Header header = new NavigationDrawer.Header("Gluon Mobile",
                "The Comments App",
                avatar);
        drawer.setHeader(header);
        
        final Item commentsItem = new ViewItem("Comments", MaterialDesignIcon.COMMENT.graphic(), COMMENTS_VIEW, ViewStackPolicy.SKIP);
        final Item editionItem = new ViewItem("Edition", MaterialDesignIcon.EDIT.graphic(), EDITION_VIEW);
        editionItem.disableProperty().bind(service.userProperty().isNull());
        
        drawer.getItems().addAll(commentsItem, editionItem);

        if (Platform.isDesktop()) {
            final Item quitItem = new Item("Quit", MaterialDesignIcon.EXIT_TO_APP.graphic());
            quitItem.selectedProperty().addListener((obs, ov, nv) -> {
                if (nv) {
                    Services.get(LifecycleService.class).ifPresent(LifecycleService::shutdown);
                }
            });
            drawer.getItems().add(quitItem);
        }
        
        drawer.addEventHandler(NavigationDrawer.ITEM_SELECTED, 
                e -> MobileApplication.getInstance().hideLayer(MENU_LAYER));
        
        MobileApplication.getInstance().viewProperty().addListener((obs, oldView, newView) -> updateItem(newView.getName()));
        updateItem(COMMENTS_VIEW);
    }
    
    private void updateItem(String nameView) {
        for (Node item : drawer.getItems()) {
            if (item instanceof ViewItem && ((ViewItem) item).getViewName().equals(nameView)) {
                drawer.setSelectedItem(item);
                break;
            }
        }
    }
    
    public NavigationDrawer getDrawer() {
        return drawer;
    }

    private Image getAvatarImage() {
        if (service != null && service.userProperty().get() != null) {
            return Service.getUserImage(service.userProperty().get().getPicture());
        } 
        return new Image(Comments20.class.getResourceAsStream("/icon.png"));
    }
}
