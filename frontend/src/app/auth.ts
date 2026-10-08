import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

// Session flag kept in sessionStorage; the server is asked to check credentials at sign-in.
const KEY = 'hpp_user';

// Backend is served through the Ultron proxy on port 8080, same host as this app.
const API = '/proxy/8080/api/auth';

export function currentUser(): string | null {
  try {
    return sessionStorage.getItem(KEY);
  } catch {
    return null;
  }
}

export async function authenticate(
  action: 'login' | 'register',
  email: string,
  password: string,
): Promise<string> {
  const res = await fetch(`${API}/${action}`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email, password }),
  });
  const body = await res.json().catch(() => ({}));
  if (!res.ok) {
    // Spring hides error messages by default, so map the status codes here.
    const messages: Record<number, string> = {
      401: 'Wrong email or password.',
      409: 'An account with this email already exists.',
      400: 'Enter a valid email and a password of at least 6 characters.',
    };
    throw new Error(messages[res.status] || 'Something went wrong. Please try again.');
  }
  try {
    sessionStorage.setItem(KEY, body.email);
  } catch {
    // storage blocked: the session just won't persist across navigation
  }
  return body.email;
}

export function logout(): void {
  try {
    sessionStorage.removeItem(KEY);
  } catch {
    // nothing to clear
  }
}

export const authGuard: CanActivateFn = () =>
  currentUser() ? true : inject(Router).createUrlTree(['/login']);
