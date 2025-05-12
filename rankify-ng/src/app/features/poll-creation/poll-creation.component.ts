import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { PollService } from '../../core/services/poll.service';
import { Poll, PollOption } from '../../core/models/poll.model';

@Component({
  selector: 'app-poll-creation',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './poll-creation.component.html',
  styleUrls: ['./poll-creation.component.scss']
})
export class PollCreationComponent {
  pollForm: FormGroup;
  submitting = false;
  error = '';

  constructor(
    private fb: FormBuilder,
    private pollService: PollService,
    private router: Router
  ) {
    this.pollForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      options: this.fb.array(
        [this.fb.control(''), this.fb.control('')],
        [Validators.minLength(2)]
      )
    });
  }

  get name() {
    return this.pollForm.get('name')!;
  }

  get options() {
    return this.pollForm.get('options') as FormArray;
  }

  addOption() {
    this.options.push(this.fb.control(''));
  }

  removeOption(index: number) {
    if (this.options.length > 2) {
      // Simply remove the option at the specified index
      this.options.removeAt(index);
    } else {
      this.error = 'A poll must have at least two options';
      setTimeout(() => (this.error = ''), 3000);
    }
  }

  get formValid(): boolean {
    // Form is valid if name is valid and all options have nonempty values
    const allOptionsValid = this.options.controls.every(
      control => control.value && control.value.trim() !== ''
    );
    const validOptionCount = this.options.controls.length >= 2;

    return this.name.valid && allOptionsValid && validOptionCount;
  }

  onSubmit() {
    if (!this.formValid) return;
    
    this.submitting = true;
    this.error = '';
    
    // Filter out any blank options that might have been left
    const validOptions = this.options.value
      .filter((option: string) => option.trim() !== '')
      .map((option: string) => ({ name: option.trim() }));
    
    if (validOptions.length < 2) {
      this.error = 'You need at least two valid options';
      this.submitting = false;
      return;
    }
    
    const newPoll: Poll = {
      id: '', // Will be set by the service (mock or backend)
      name: this.name.value.trim(),
      isOpen: false, // Initially closed
      options: validOptions as PollOption[],
      createdAt: new Date() // Will be overwritten by the service
    };
    
    this.pollService.createPoll(newPoll).subscribe({
      next: (createdPoll) => {
        this.submitting = false;
        this.router.navigate(['/polls', createdPoll.name]);
      },
      error: (err) => {
        this.submitting = false;
        this.error = err.message || 'Failed to create poll. Please try again.';
      }
    });
  }

  goBack() {
    this.router.navigate(['/']);
  }
}
