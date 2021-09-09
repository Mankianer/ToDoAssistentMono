import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  hide = true;
  private loginUrl = environment.apiUrl + environment.loginPath;

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
  }

  login(username: string, password: string) {
    console.log('username:' + username);

    this.http.post(this.loginUrl, {username: username, password: password}, {withCredentials: true}).toPromise()
    .then(value => console.log('login successful'))
    .catch(error => console.log('login error: ' + error));
  }

}
