import { Component, OnInit } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';
import { keycloak } from '../../../auth/keycloak.init';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, CommonModule],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  username = '';

  ngOnInit(): void {
    this.username = keycloak.tokenParsed?.['preferred_username'] || '';
  }

  logout(): void {
    keycloak.logout({ redirectUri: 'http://localhost:4200' });
  }
}