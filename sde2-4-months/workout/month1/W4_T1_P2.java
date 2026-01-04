import java.util.*;


class BrowserHistory{
    private Stack<String> backStack = new Stack<>();
    private Stack<String> forwardStack = new Stack<>();
    public String currentPage = "";

    public String back(){
        if (backStack.isEmpty()) return  currentPage;
        forwardStack.push(currentPage);
        currentPage = backStack.pop();
        return currentPage;
    }

    public String forward(){
        if (forwardStack.isEmpty()) return currentPage;
        backStack.push(currentPage);
        currentPage = forwardStack.pop();
        return currentPage;
    }

    public void visit(String page) {
        if (!currentPage.isEmpty()) {
            backStack.push(currentPage);
        }
        currentPage = page;
        forwardStack.clear();
    }
}

public class W4_T1_P2 {


     public static void main(String[] args){
        BrowserHistory browser = new BrowserHistory();
        browser.visit("google.com");
        System.out.println(browser.currentPage);
        browser.visit("facebook.com");
        System.out.println(browser.currentPage);
        browser.visit("instagram.com");
        System.out.println(browser.currentPage);
        browser.back();
        System.out.println(browser.currentPage);
        browser.forward();
        System.out.println(browser.currentPage);
        browser.visit("twitter.com");
        System.out.println(browser.currentPage);
        browser.back();
        browser.back();
        System.out.println(browser.currentPage);
        browser.back();
        browser.back();
        browser.back();
        System.out.println(browser.currentPage);
        browser.forward();
        System.out.println(browser.currentPage);


    }
    
    
}
