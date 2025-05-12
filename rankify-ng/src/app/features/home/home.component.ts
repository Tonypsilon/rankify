import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { PollService } from '../../core/services/poll.service';
import { Poll } from '../../core/models/poll.model';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  searchTerm: string = '';
  polls: Poll[] = [];
  loading = false;
  error = '';

  constructor(private pollService: PollService) {}

  ngOnInit(): void {
    // No initial load; users search for polls by name
  }

  searchPoll(): void {
    if (!this.searchTerm) {
      return;
    }
    this.loading = true;
    this.error = '';
    this.polls = [];
    this.pollService.getPollByName(this.searchTerm).subscribe({
      next: (poll) => {
        this.polls = [poll];
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.error = `No poll found with name "${this.searchTerm}"`;
      }
    });
  }

  loadPolls(): void {
    this.pollService.getAllPolls().subscribe({
      next: (polls) => {
        this.polls = polls;
        this.loading = false;
      },
      error: (err) => {
        this.error = err.message || 'Failed to load polls';
        this.loading = false;
      }
    });
  }
}
