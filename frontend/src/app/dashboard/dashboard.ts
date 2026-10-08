import { Component } from '@angular/core';
import { Router } from '@angular/router';

import { currentUser, logout } from '../auth';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class Dashboard {
  readonly user = currentUser();

  constructor(private router: Router) {}

  signOut(): void {
    logout();
    this.router.navigate(['/login']);
  }
}
