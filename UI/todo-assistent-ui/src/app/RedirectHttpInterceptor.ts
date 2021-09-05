import {Injectable} from "@angular/core";
import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest
} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {catchError, retry, tap} from "rxjs/operators";
import {Router} from "@angular/router";

@Injectable()
export class RedirectHttpInterceptor implements HttpInterceptor {

  constructor(private router: Router) {

  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(retry(0), catchError((error: HttpErrorResponse) => {
      if (error.status === 401) {
        this.router.navigate(['/login']);
      }
      return throwError(error);
    }));
  }
}
