# Codefellowship App  
Use these dependencies    
* testImplementation 'org.springframework.boot:spring-boot-starter-test'  
* testImplementation 'org.springframework.security:spring-security-test'  
So can use the spring security ( Authentication and authurization)  
  
# The End Points  
* **/login** : login Form sucess login redirect to the **/myprofile** end point which shows the user information and his/her posts, Otherwise **/login** again.   
* **/signup** : signup Form, and then redirect to */myprofile* end point.  
* **/logout** : logout from the App. 
* **/myprofile** : show the information for the logged user, his/her posts, and the users who is following.   
* **/post** : the logged in user can add posts to his/her profile, and redirect it to the */myprofile*.
* **/users** : the logged in user can select application user, and viewing the data about this User.  
  
* **/users/follow** : List for all the application user with follow and unfollow button, when click on follow will redirect to the */myprofile. And show Account button to show the profile for specific user. and will redirect to */users/account*.  
* **/users/account** : show the information, posts and the following list for specific user.     
* **users/unfollow** : when click unfollow button will remove this user from the following List for the loggedin user, and redirect to the */myprofile*.  
* **/feed** : show the posts which belongs to the users which the loggedin user follow.  
* **/** : the home page.  

# WebSecurityConfig File  
 @Override  
    protected void configure(HttpSecurity http) throws Exception {  
        http.cors().disable().csrf().disable()  
                .authorizeRequests()  
                .antMatchers("/login*").permitAll() // any one can access  
                .antMatchers("/signup*").permitAll() // any one can access  
                .antMatchers("/").permitAll() // any one can access  
                .antMatchers("/style.css").permitAll()// any one can access  
                .anyRequest().authenticated()  
                .and()  
                .formLogin()  
                .loginPage("/login")  
                .loginProcessingUrl("/perform_login") // process the login after submit  
                .defaultSuccessUrl("/myprofile") // success login go to route /hello  
                .failureUrl("/login") // Failure login go back to login  
                .and()  
                .logout()  
                .logoutUrl("/perform_logout") //  process the logout after submit  
                .logoutSuccessUrl("/login") // success logout go back to login  
                .deleteCookies("JSESSIONID");  
    }  
# application.properties File
spring.datasource.platform=postgres  
spring.datasource.url=jdbc:postgresql://localhost:5432/fellowship  
spring.datasource.username=alaa  
spring.datasource.password=  
spring.jpa.hibernate.ddl-auto=update  
spring.datasource.initialization-mode=always  


