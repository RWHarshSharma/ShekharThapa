import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import { authenticate } from '../auth';

@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {
  email = '';
  password = '';
  error = '';
  mode: 'login' | 'register' = 'login';
  busy = false;

  constructor(private router: Router) {}

  toggleMode(): void {
    this.mode = this.mode === 'login' ? 'register' : 'login';
    this.error = '';
  }

  async submit(): Promise<void> {
    if (this.busy) return;
    this.busy = true;
    this.error = '';
    try {
      await authenticate(this.mode, this.email.trim(), this.password);
      this.router.navigate(['/dashboard']);
    } catch (e) {
      this.error = e instanceof Error ? e.message : 'Something went wrong. Please try again.';
    } finally {
      this.busy = false;
    }
  }
}
