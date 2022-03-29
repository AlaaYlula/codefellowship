# Codefellowship App  
Use these dependencies    
* testImplementation 'org.springframework.boot:spring-boot-starter-test'  
* testImplementation 'org.springframework.security:spring-security-test'  
So can use the spring security ( Authentication and authurization)  
  
# The End Points  
* **/login** : login Form sucess login redirect to the **/hello** end point, Otherwise **/login** again.   
* **/signup** : signup Form, and then redirect to **/hello** end point.  
* **/logout** : logout from the App.  
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
                .defaultSuccessUrl("/hello") // success login go to route /hello  
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


