package com.example.app.controller;
import java.util.ArrayList;
import java.util.List;

import com.example.app.dto.UserRequest;
import com.example.app.dto.UserSearchRequest;
import com.example.app.dto.UserUpdateRequest;
import com.example.app.entity.User;
import com.example.app.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * ユーザー情報 Controller
 */
@Controller
public class UserController {
  /**
   * ユーザー情報 Service
   */
  @Autowired
  UserService userService;
  /**
   * ユーザー情報検索画面を表示
   * @param model Model
   * @return ユーザー情報一覧画面
   */
  @GetMapping(value = "/user/search")
  public String displaySearch(Model model) {
    return "user/search";
  }
  /**
   * ユーザー情報検索
   * @param userSearchRequest リクエストデータ
   * @param model Model
   * @return ユーザー情報一覧画面
   */
  @RequestMapping(value = "/user/id_search", method = RequestMethod.POST)
  public String search(@ModelAttribute UserSearchRequest userSearchRequest, Model model) {
    User user = userService.search(userSearchRequest);
    model.addAttribute("userinfo", user);
    return "user/search";
  }
  /**
   * ユーザー新規登録画面を表示
   * @param model Model
   * @return ユーザー情報一覧画面
   */
   @RequestMapping(value = "/user/add", method = RequestMethod.GET)
   public String displayAdd(Model model) {
       model.addAttribute("userRequest", new UserRequest());
       return "user/add";
   }
   /**
   * ユーザー新規登録
   * @param userRequest リクエストデータ<br>    * @param result BindingResultl
   * @param model Model
   * @return ユーザー情報一覧画面
   */
   @RequestMapping(value = "/user/create", method = RequestMethod.POST)
   public String create(@Validated @ModelAttribute UserRequest userRequest, BindingResult result, Model model) {
       if (result.hasErrors()) {
           List<String> errorList = new ArrayList<String>();
           for (ObjectError error : result.getAllErrors()) {
               errorList.add(error.getDefaultMessage());
           }
           model.addAttribute("validationError", errorList);
           return "user/add";
       }
       // ユーザー情報の登録
       userService.create(userRequest);
       return "redirect:/user/list";
     }

   /**
    * ユーザー情報一覧画面を表示
    * @param model Model
    * @return ユーザー情報一覧画面
    */
   @GetMapping(value = "/user/list")
   public String displayList(Model model) {
     List<User> userlist = userService.searchAll();
     model.addAttribute("userlist", userlist);
     return "user/list";
   }
   /**
    * ユーザー情報詳細画面を表示
    * @param id 表示するユーザーID
    * @param model Model
    * @return ユーザー情報詳細画面
    */
   @GetMapping("/user/{id}")
   public String displayView(@PathVariable Long id, Model model) {
     User user = userService.findById(id);
     model.addAttribute("userData", user);
     return "user/view";
   }
   /**
    * ユーザー編集画面を表示
    * @param id 表示するユーザーID
    * @param model Model
    * @return ユーザー編集画面
    */
   @GetMapping("/user/{id}/edit")
   public String displayEdit(@PathVariable Long id, Model model) {
     User user = userService.findById(id);
     UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
     userUpdateRequest.setId(user.getId());
     userUpdateRequest.setName(user.getName());
     userUpdateRequest.setPhone(user.getPhone());
     userUpdateRequest.setAddress(user.getAddress());
     model.addAttribute("userUpdateRequest", userUpdateRequest);
     return "user/edit";
   }
   /**
    * ユーザー更新
    * @param userRequest リクエストデータ
    * @param model Model
    * @return ユーザー情報詳細画面
    */
   @RequestMapping(value = "/user/update", method = RequestMethod.POST)
   public String update(@Validated @ModelAttribute UserUpdateRequest userUpdateRequest, BindingResult result, Model model) {
     if (result.hasErrors()) {
       List<String> errorList = new ArrayList<String>();
       for (ObjectError error : result.getAllErrors()) {
         errorList.add(error.getDefaultMessage());
       }
       model.addAttribute("validationError", errorList);
       return "user/edit";
     }
     // ユーザー情報の更新
     userService.update(userUpdateRequest);
     return String.format("redirect:/user/%d", userUpdateRequest.getId());
   }
   /**
    * ユーザー情報削除
    * @param id 表示するユーザーID
    * @param model Model
    * @return ユーザー情報詳細画面
    */
   @GetMapping("/user/{id}/delete")
   public String delete(@PathVariable Long id, Model model) {
     // ユーザー情報の削除
     userService.delete(id);
     return "redirect:/user/list";
   }
}