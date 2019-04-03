package geacommerce.web;

import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseController {

    protected ModelAndView view(String viewName) {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("fragments/base-layout");
        modelAndView.addObject("view", viewName);

        return modelAndView;
    }

    protected ModelAndView view(String viewName, String objectName, Object object) {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("fragments/base-layout");
        modelAndView.addObject("view", viewName);
        modelAndView.addObject(objectName, object);

        return modelAndView;
    }

    protected ModelAndView view(String viewName, String objectName, Object object, String secondObjectName, Object secondObject) {
        ModelAndView modelAndView = new ModelAndView();
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put(objectName, object);
        objectMap.put(secondObjectName, secondObject);

        modelAndView.setViewName("fragments/base-layout");
        modelAndView.addObject("view", viewName);
        modelAndView.addAllObjects(objectMap);

        return modelAndView;
    }

    protected ModelAndView view(String viewName, String objectName, Object object, String secondObjectName, Object secondObject, String thirdObjectName, Object thirdObject) {
        ModelAndView modelAndView = new ModelAndView();
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put(objectName, object);
        objectMap.put(secondObjectName, secondObject);
        objectMap.put(thirdObjectName, thirdObject);

        modelAndView.setViewName("fragments/base-layout");
        modelAndView.addObject("view", viewName);
        modelAndView.addAllObjects(objectMap);

        return modelAndView;
    }

    protected ModelAndView redirect(String url) {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("redirect:" + url);

        return modelAndView;
    }

    protected ModelAndView redirect(String url, String objectName, Object object) {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject(objectName, object);

        modelAndView.setViewName("redirect:" + url);

        return modelAndView;
    }
}

