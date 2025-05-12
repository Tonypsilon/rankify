import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { PollService } from '../../core/services/poll.service';
import { BallotService } from '../../core/services/ballot.service';
import { Poll, Ballot } from '../../core/models/poll.model';

@Component({
  selector: 'app-poll-management',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './poll-management.component.html',
  styleUrls: ['./poll-management.component.scss']
})
export class PollManagementComponent implements OnInit {
  poll: Poll | null = null;
  ballots: Ballot[] = [];
  rankSummary: { optionName: string, rankCounts: number[] }[] = [];
  maxVotes: number = 0;
  error = '';
  successMessage = '';
  updating = false;
  loadingBallots = false;
  
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private pollService: PollService,
    private ballotService: BallotService
  ) {}
  
  ngOnInit(): void {
    const pollName = this.route.snapshot.paramMap.get('pollName');
    if (!pollName) {
      this.error = 'Poll name is required';
      return;
    }
    
    this.loadPoll(pollName);
  }
  
  loadPoll(pollName: string): void {
    this.pollService.getPollByName(pollName).subscribe({
      next: (poll) => {
        this.poll = poll;
        this.loadBallots(poll.id);
      },
      error: (err) => {
        this.error = err.message || 'Failed to load poll';
      }
    });
  }
  
  loadBallots(pollId: string): void {
    this.loadingBallots = true;
    
    this.ballotService.getBallotsByPollId(pollId).subscribe({
      next: (ballots) => {
        this.ballots = ballots;
        this.loadingBallots = false;
        this.calculateRankSummary();
      },
      error: (err) => {
        this.error = err.message || 'Failed to load ballots';
        this.loadingBallots = false;
      }
    });
  }
  
  calculateRankSummary(): void {
    if (!this.poll || this.ballots.length === 0) return;
    
    // Initialize rank summary for each option
    this.rankSummary = this.poll.options.map(option => ({
      optionName: option.name,
      rankCounts: Array(this.poll!.options.length).fill(0) // Initialize with zeros for each possible rank
    }));
    
    // Count votes for each option at each rank
    this.ballots.forEach(ballot => {
      ballot.rankings.forEach(ranking => {
        const optionIndex = this.poll!.options.findIndex(o => o.id === ranking.optionId);
        if (optionIndex !== -1 && ranking.rank - 1 < this.rankSummary[optionIndex].rankCounts.length) {
          this.rankSummary[optionIndex].rankCounts[ranking.rank - 1]++;
        }
      });
    });
    
    // Find maximum vote count for scaling the visualization
    this.maxVotes = Math.max(
      ...this.rankSummary.flatMap(summary => summary.rankCounts)
    );
    
    // If no votes, set to 1 to avoid division by zero
    this.maxVotes = this.maxVotes || 1;
  }
  
  togglePollStatus(): void {
    if (!this.poll) return;
    
    this.updating = true;
    this.error = '';
    this.successMessage = '';
    
    const operation = this.poll.isOpen 
      ? this.pollService.stopPoll(this.poll.id)
      : this.pollService.startPoll(this.poll.id);
    
    operation.subscribe({
      next: (updatedPoll) => {
        this.poll = updatedPoll;
        this.updating = false;
        this.successMessage = `Poll ${this.poll.isOpen ? 'opened' : 'closed'} successfully`;
        setTimeout(() => this.successMessage = '', 3000);
      },
      error: (err) => {
        this.updating = false;
        this.error = err.message || `Failed to ${this.poll!.isOpen ? 'close' : 'open'} poll`;
      }
    });
  }
  
  copyPollLink(): void {
    if (!this.poll) return;
    
    const pollUrl = `${window.location.origin}/cast-ballot/${this.poll.name}`;
    navigator.clipboard.writeText(pollUrl).then(
      () => {
        this.successMessage = 'Poll link copied to clipboard!';
        setTimeout(() => this.successMessage = '', 3000);
      },
      () => {
        this.error = 'Failed to copy poll link';
      }
    );
  }
}
