import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  hide = true;
  private loginUrl = environment.apiUrl + environment.loginPath;
  private logoutUrl = environment.apiUrl + environment.logoutPath;
  private isLoggInUrl = environment.apiUrl + environment.isloggedInPath;
  public isLoggedIn = false;

  constructor(private http: HttpClient, private _snackBar: MatSnackBar) {
    this.http.get<boolean>(this.isLoggInUrl, {withCredentials: true}).subscribe(value => this.isLoggedIn = value);
  }

  ngOnInit(): void {

  }

  login(username: string, password: string) {
    this.http.post(this.loginUrl, {
      username: username,
      password: password
    }, {withCredentials: true}).toPromise()
    .then(value => {
      console.log('login successful');
      window.location.reload();
    })
    .catch(error => {
      console.log(error);
      this.showError(error.error.status == 401 ? 'Wrong Username or Password' : 'Unknown Error: ' + error.message);
    });
  }

  logout() {
    this.http.delete(this.logoutUrl, {withCredentials: true}).toPromise()
    .then(value => {
      console.log('login successful');
      window.location.reload();
    })
    .catch(error => {
      console.log(error)
      this.showError(error.message);
    });

  }

  showError(error: string) {
    this._snackBar.open(error, "close");
  }
}
