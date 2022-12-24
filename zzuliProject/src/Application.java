import controller.AdminBookController;
import controller.AdminMesController;
import controller.UserBookController;
import controller.UserMesController;
import model.Order;
import model.RestaurantDao;
import model.UserDao;

import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        Application application =new Application();
        application.menuMain();
    }

    public void menuMain() {
        //选择进入用户子系统还是商家子系统
        System.out.println("================请选择您的身份================");
        System.out.println("输入1：用户");
        System.out.println("输入2：商家");
        System.out.println("输入：");
        //获得输入的选择
        int choice = inInt();
        if(choice==1){
            //调用显示用户子系统
            menuUser();
        }else{
            //调用显示商家子系统
            menuAdmin();
        }
    }


    /**
     * 用户子系统界面显示
     */
    public void menuUser(){
        //先让用户进行登录
        System.out.println("================用户系统================");
        System.out.println("\t\t\t\t登录（1）");
        System.out.println("\t\t\t\t注册（2）");
        System.out.println("\t\t\t\t返回（0）");
        System.out.println("请输入您的选择：");
        int choice = inInt();
        if(choice==1){
            UserDao userDao=new UserDao();
            System.out.println("请输入账号：");
            String userName = inString();
            System.out.println("请输入密码：");
            String password = inString();
            boolean isUser = userDao.userLogin(userName,password);
            //如果账号密码存在并且正确
            if(isUser){
                System.out.println("================欢迎登录用户管理系统================");
                System.out.println("\t\t\t\t个人信息管理(1)");
                System.out.println("\t\t\t\t餐馆查询(2)");
                System.out.println("\t\t\t\t餐馆预定(3)");
                System.out.println("\t\t\t\t返回(0)");
                System.out.println("请输入您的选择：");
                int userChoice = inInt();
                switch (userChoice){
                    case 0:
                        menuUser();
                        break;
                    case 1:
                        userController(userName);
                        break;
                    case 2:
                        UserBookController.findRes(userName);
                        break;
                    case 3:
                        restaurantBook(userName);
                        break;
                }
            }else{
                System.out.println("账号或密码输入错误，请重新登录");
            }
        }else if(choice==2){
            System.out.println("请输入注册账号：");
            int userNameApply = inInt();
            System.out.println("请输入您的密码：");
            int passwordApply = inInt();
        }else if(choice==0){
            menuMain();
        }
    }


    /**
     * 用户子系统的用户管理功能
     * @param userName
     */
    private void userController(String userName) {
        UserMesController controller=new UserMesController();
        System.out.println("================用户个人信息管理================");
        System.out.println("\t\t\t\t查看个人信息(1)");
        System.out.println("\t\t\t\t修改个人信息(2)");
        System.out.println("\t\t\t\t注销个人信息(3)");
        System.out.println("\t\t\t\t返回(0)");
        System.out.println("请输入您的选择：");
        int choice = inInt();
        switch (choice){
            case 0:
                menuUser();
                break;
            case 1:
                controller.selectUser(userName);
                break;
            case 2:
                controller.updateUser(userName);
                break;
            case 3:
                controller.deleteUser(userName);
                break;
        }
    }

    /**
     * 用户子系统的餐馆预定功能
     * @param userName
     */
    private void restaurantBook(String userName) {
        UserBookController controller=new UserBookController();
        System.out.println("\t\t\t\t查询用户所有预定(1)");
        System.out.println("\t\t\t\t查询某餐馆的预定(2)");
        System.out.println("\t\t\t\t添加预定(3)");
        System.out.println("\t\t\t\t返回(0)");
        int choice = inInt();
        switch (choice){
            case 0:
                menuUser();
                break;
            case 1:
                controller.searchUserOrder(userName);
                break;
            case 2:
                System.out.println("请输入要查询餐馆的名称：");
                String name=inString();
                controller.searchResOrder(name);
                break;
            case 3:

                Order order=new Order();
                controller.addOrder(order);
                break;
        }
    }

    /**
     * 餐馆子系统界面显示
     */
    public void menuAdmin() {
        RestaurantDao restaurantDao=new RestaurantDao();
        System.out.println("================商家登录系统================");
        System.out.println("请输入账号：");
        String adminName = inString();
        System.out.println("请输入密码：");
        String adminPassword = inString();
        boolean isAdmin = restaurantDao.adminLogin(adminName,adminPassword);
        if(isAdmin){
            System.out.println("================欢迎登录商家管理系统================");
            System.out.println("\t\t\t\t餐馆信息管理（1）");
            System.out.println("\t\t\t\t餐馆预定信息管理（2）");
            int adminChoice = inInt();
            switch (adminChoice){
                case 1:
                    messContro(adminName);
                    break;
                case 2:
                    bookContro(adminName);
                    break;
            }
        }
    }

    /**
     * 餐馆信息管理
     * @param username
     */
    private void messContro(String username) {
        AdminMesController controller=new AdminMesController();
        System.out.println("\t\t\t\t查询餐馆信息（1）");
        System.out.println("\t\t\t\t修改餐馆信息（2）");
        System.out.println("\t\t\t\t注销餐馆信息（3）");

        System.out.println("请输入您的选择：");
        int choice = inInt();
        switch (choice){
            case 1:
                controller.selectUser(username);
                break;
            case 2:
                controller.updateUser(username);
                break;
            case 3:
                controller.deleteUser(username);
                break;
        }
    }

    /**
     * 预定信息管理
     * @param username
     */
    private void bookContro(String username) {
        AdminBookController controller=new AdminBookController();
        System.out.println("\t\t\t\t查询餐馆所有预定(1)");
        System.out.println("\t\t\t\t查询某用户预定(2)");
        System.out.println("\t\t\t\t处理预定(3)");
        int choice = inInt();
        switch (choice){
            case 1:
                controller.searchAllOrder(username);
                break;
            case 2:
                controller.searchUserOrder(username);
                break;
            case 3:
                controller.doOrder(1);
                break;
        }
    }


    /**
     * 在控制台输入一个数字，并将这个数字返回
     * @return
     */
    public static int inInt(){
        Scanner in = new Scanner(System.in);
        int choice = in.nextInt();
        return choice;
    }


    /**
     * 在控制台输入字符串，并将这个字符串返回
     * @return
     */
    public static String inString(){
        Scanner in = new Scanner(System.in);
        String choice = in.nextLine();
        return choice;
    }
}

