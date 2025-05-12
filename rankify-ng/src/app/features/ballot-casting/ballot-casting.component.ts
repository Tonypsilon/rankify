import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { CdkDragDrop, DragDropModule, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { PollService } from '../../core/services/poll.service';
import { BallotService } from '../../core/services/ballot.service';
import { Poll, PollOption, Ballot, Ranking } from '../../core/models/poll.model';

@Component({
  selector: 'app-ballot-casting',
  standalone: true,
  imports: [CommonModule, RouterLink, DragDropModule],
  templateUrl: './ballot-casting.component.html',
  styleUrls: ['./ballot-casting.component.scss']
})
export class BallotCastingComponent implements OnInit {
  poll: Poll | null = null;
  rankGroups: PollOption[][] = [[]];
  unrankedOptions: PollOption[] = [];
  submitting = false;
  error = '';
  successMessage = '';
  
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
        
        if (!poll.isOpen) {
          return;
        }
        
        // Initialize with all options unranked
        this.unrankedOptions = [...poll.options];
        this.rankGroups = [[]]; // Start with one empty rank group
      },
      error: (err) => {
        this.error = err.message || 'Failed to load poll';
      }
    });
  }
  
  drop(event: CdkDragDrop<PollOption[]>): void {
    if (event.previousContainer.data === event.container.data) {
      // Reordering within the same rank group
      moveItemInArray(
        event.container.data,
        event.previousIndex,
        event.currentIndex
      );
    } else {
      // Moving from one list to another
      transferArrayItem(
        event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex
      );
      
      // Only cleanup empty groups and auto-add new blank when dragging from unranked
      if (event.previousContainer.data === this.unrankedOptions) {
        this.cleanupEmptyGroups();
        // Automatically add a new empty rank group when all current groups have at least one entry
        if (this.rankGroups.length > 0 && this.rankGroups.every(group => group.length > 0)) {
          this.rankGroups.push([]);
        }
      }
    }
  }
  
  cleanupEmptyGroups(): void {
    // Keep at least one rank group, even if it's empty
    if (this.rankGroups.length > 1) {
      this.rankGroups = this.rankGroups.filter(group => group.length > 0);
      
      // If all were filtered out, add one empty group back
      if (this.rankGroups.length === 0) {
        this.rankGroups = [[]];
      }
    }
  }
  
  addRankGroup(): void {
    this.rankGroups.push([]);
  }
  
  resetRanking(): void {
    if (!this.poll) return;
    
    this.unrankedOptions = [...this.poll.options];
    this.rankGroups = [[]];
  }
  
  isAllUnranked(): boolean {
    return this.rankGroups.every(group => group.length === 0);
  }
  
  submitBallot(): void {
    if (!this.poll || !this.poll.id) return;
    
    this.submitting = true;
    this.error = '';
    
    // Convert the rank groups to rankings
    const rankings: Ranking[] = [];
    
    this.rankGroups.forEach((group, groupIndex) => {
      group.forEach(option => {
        rankings.push({
          optionId: option.id,
          rank: groupIndex + 1
        });
      });
    });
    
    const ballot: Ballot = {
      id: '',  // Will be set by the service
      pollId: this.poll.id,
      rankings: rankings
    };
    
    this.ballotService.castBallot(ballot).subscribe({
      next: () => {
        this.submitting = false;
        this.successMessage = 'Your ballot has been cast successfully!';
      },
      error: (err) => {
        this.submitting = false;
        this.error = err.message || 'Failed to cast ballot. The poll might be closed.';
      }
    });
  }
  
  getConnectedLists(): string[] {
    return this.rankGroups.map((_, i) => 'rank-' + i);
  }

  /**
   * Returns all drop-list IDs except the one at index, plus the unranked list,
   * to allow dragging between any two rank levels and the unranked list.
   */
  getConnectedListsFor(index: number): string[] {
    const ids = this.rankGroups
      .map((_, i) => 'rank-' + i)
      .filter((_, i) => i !== index);
    ids.push('unranked');
    return ids;
  }
}
